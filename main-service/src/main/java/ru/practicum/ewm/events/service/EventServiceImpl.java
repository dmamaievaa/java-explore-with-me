package ru.practicum.ewm.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.NewEventDto;
import ru.practicum.ewm.events.dto.SearchEventParamsAdmin;
import ru.practicum.ewm.events.dto.SearchEventParamsPublic;
import ru.practicum.ewm.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.enums.SortType;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.enums.UserState;
import ru.practicum.ewm.events.model.Location;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.events.repository.LocationRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.events.mapper.EventMapper;
import ru.practicum.ewm.exception.ProblemOfEditingException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventSpec;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.utils.Constants.EVENT_NOT_FOUND_MESSAGE;
import static ru.practicum.ewm.utils.Constants.CAT_NOT_FOUND_MESSAGE;
import static ru.practicum.ewm.utils.Constants.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventStatService eventStatService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> getFromAdmin(SearchEventParamsAdmin searchEventParamsAdmin) {
        PageRequest pageable = PageRequest.of(searchEventParamsAdmin.getFrom() / searchEventParamsAdmin.getSize(),
                searchEventParamsAdmin.getSize());
        Specification<Event> spec = EventSpec.getAdminFilters(searchEventParamsAdmin);
        Page<Event> events = eventRepository.findAll(spec, pageable);
        return events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateByAdmin(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        eventMapper.updateEventFromAdminRequest(event, updateEventAdminRequest);
        Event updatedEvent = eventRepository.save(event);

        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> getFromPublic(SearchEventParamsPublic searchEventPublicParams, HttpServletRequest request) {

        log.info("Request URI: {}", request.getRequestURI());

        if (searchEventPublicParams.getRangeStart() != null && searchEventPublicParams.getRangeEnd() != null
                && searchEventPublicParams.getRangeStart().isAfter(searchEventPublicParams.getRangeEnd())) {
            throw new BadRequestException("The end of the range cannot be earlier than the start of the range");
        }

        Specification<Event> spec = EventSpec.getPublicFilters(searchEventPublicParams);
        Sort sorting = Sort.by("eventDate");

        if (searchEventPublicParams.getSort() != null) {
            try {
                SortType sortType = SortType.valueOf(searchEventPublicParams.getSort().toUpperCase());
                if (sortType == SortType.EVENT_DATE) {
                    sorting = Sort.by("eventDate");
                } else if (sortType == SortType.VIEWS) {
                    sorting = Sort.by("views");
                }
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("The provided sort value is invalid: " + searchEventPublicParams.getSort());
            }
        }
        Pageable pageable = PageRequest.of(searchEventPublicParams.getFrom() / searchEventPublicParams.getSize(), searchEventPublicParams.getSize(), sorting);

        Page<Event> events = eventRepository.findAll(spec, pageable);
        eventStatService.sendStatData(request);

        List<Event> updatedEvents = events.getContent().stream()
                .peek(event -> {
                    Integer views = eventStatService.getUniqueViews(event, request.getRequestURI()) + 1;
                    event.setViews(views);
                })
                .toList();

        eventRepository.saveAll(updatedEvents);

        return events.getContent().stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getById(Integer eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId));
        }

        Integer views = eventStatService.getUniqueViews(event, request.getRequestURI());

        event.setViews(views);

        event = eventRepository.save(event);

        EventFullDto fullDto = eventMapper.toEventFullDto(event);

        eventStatService.sendStatData(request);
        return fullDto;
    }

    @Override
    public List<EventShortDto> getByCurrentUser(Integer userId, int from, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> page = eventRepository.findAllByInitiator(user, pageable);

        return page.isEmpty() ? List.of() :
                page.getContent().stream()
                        .map(eventMapper::toShortDto)
                        .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addFromPrivate(Integer userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        LocalDateTime timeValForEvent = LocalDateTime.now().plusHours(2);
        if (newEventDto.getEventDate().isBefore(timeValForEvent)) {
            throw new BadRequestException("The event date must be no earlier than %s.");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format(CAT_NOT_FOUND_MESSAGE, newEventDto.getCategory() )));

        Location location = newEventDto.getLocation();
        if (location != null && location.getId() == null) {
            location = locationRepository.save(location);
        }

        Event event = eventMapper.fromNewEventDto(newEventDto, category, user);
        event.setLocation(location);

        event = eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto getFullByCurrentUser(Integer userId, Integer eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateByCurrentUser(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ProblemOfEditingException("Published events cannot be edited");
        }

        eventMapper.updateEventFromPrivateRequest(event, updateEventUserRequest, categoryRepository);

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(UserState.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
            if (updateEventUserRequest.getStateAction().equals(UserState.CANCEL_REVIEW)
                    && event.getState().equals(State.PENDING)) {
                event.setState(State.CANCELED);
            }
        }

        event = eventRepository.save(event);

        return eventMapper.toEventFullDto(event);
    }


    @Override
    public List<ParticipationRequestDto> getRequestsByCurrentUser(Integer userId, Integer eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        Collection<ParticipationRequest> requests = requestRepository.findByEvent(event);

        return requests.isEmpty()
                ? List.of()
                : requests.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(Integer userId,
                                                       Integer eventId,
                                                       EventRequestStatusUpdateRequest statusUpdateRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requestRepository.findByEvent(event).stream().map(requestMapper::toParticipationRequestDto).toList())
                    .rejectedRequests(List.of())
                    .build();
        }

        Optional<ParticipationRequest> pendingRequestOpt = requestRepository
                .findByEventAndStatus(event, RequestStatus.PENDING)
                .stream()
                .findFirst();

        ParticipationRequest pendingRequest = pendingRequestOpt
                .orElseThrow(() -> new ProblemOfEditingException("The request must be in a PENDING status"));

        Integer confirmedRequests = requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);

        if (statusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED) && confirmedRequests >= event.getParticipantLimit()) {
            throw new ConflictException("The maximum participant limit has been reached");
        }

        if (confirmedRequests < event.getParticipantLimit()) {
            pendingRequest.setStatus(statusUpdateRequest.getStatus());
            requestRepository.save(pendingRequest);

            if (statusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                confirmedRequests++;
            }
        } else {
            pendingRequest.setStatus(RequestStatus.REJECTED);
            requestRepository.save(pendingRequest);
        }

        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(requestRepository.findByEventAndStatus(event, RequestStatus.CONFIRMED).stream()
                        .map(requestMapper::toParticipationRequestDto)
                        .toList())
                .rejectedRequests(requestRepository.findByEventAndStatus(event, RequestStatus.REJECTED).stream()
                        .map(requestMapper::toParticipationRequestDto)
                        .toList())
                .build();
    }
}