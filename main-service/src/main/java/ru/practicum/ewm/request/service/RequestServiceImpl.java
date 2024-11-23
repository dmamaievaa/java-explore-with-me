package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.exception.EventParticipationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.EVENT_NOT_FOUND_MESSAGE;
import static ru.practicum.ewm.utils.Constants.REQUEST_NOT_FOUND_MESSAGE;
import static ru.practicum.ewm.utils.Constants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> get(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
        Collection<ParticipationRequest> requests = requestRepository.findByRequester(user);

        if (requests.isEmpty()) {
            return List.of();
        }

        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (event.getInitiator().equals(user)) {
            throw new EventParticipationException(String.format("User with ID=%d cannot participate in their own event with ID=%d.", userId, eventId));
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventParticipationException(String.format("Event with ID=%d is not published. User with ID=%d cannot request participation.", userId, eventId));
        }

        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new EventParticipationException(String.format("The maximum participant limit was reached fo event with ID=%d (%d requests).", eventId, event.getParticipantLimit()));
        }

        RequestStatus status = event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED;

        if (event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }

        ParticipationRequest request = ParticipationRequest.builder().created(LocalDateTime.now()).event(event).requester(user).status(status).build();

        request = requestRepository.save(request);

        if (status.equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Integer userId, Integer requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, requestId)));

        if (!request.getRequester().equals(user)) {
            throw new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, requestId));
        }

        request.setStatus(RequestStatus.CANCELED);

        request = requestRepository.save(request);

        return requestMapper.toParticipationRequestDto(request);
    }
}