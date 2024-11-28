package ru.practicum.ewm.events.mapper;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.LocationDto;
import ru.practicum.ewm.events.dto.NewEventDto;
import ru.practicum.ewm.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.events.enums.StateAdmin;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Location;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ProblemOfEditingException;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.CAT_NOT_FOUND_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final UserMapper userMapper;

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .location(toLocationDto(event.getLocation()))
                .state(event.getState())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .views(event.getViews())
                .likes(event.getLikes())
                .dislikes(event.getDislikes())
                .rating(event.getRating())
                .likesHidden(event.isLikesHidden())
                .build();
    }

    public EventFullDto toEventFullDtoWithoutReactions(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .location(toLocationDto(event.getLocation()))
                .state(event.getState())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .views(event.getViews())
                .likesHidden(event.isLikesHidden())
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .likes(event.getLikes())
                .dislikes(event.getDislikes())
                .rating(event.getRating())
                .build();
    }

    public EventShortDto toEventShortDtoWithoutReactions(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .likes(event.getLikes())
                .dislikes(event.getDislikes())
                .rating(event.getRating())
                .build();
    }

    public Event fromNewEventDto(NewEventDto newEventDto,
                                 Category category,
                                 User initiator) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .views(0)
                .likes(0)
                .dislikes(0)
                .likesHidden(newEventDto.isLikesHidden())
                .build();
    }

    public void updateEventFromAdminRequest(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        StringBuilder updatedFieldsLog = new StringBuilder();

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
            updatedFieldsLog.append("Title|");
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
            updatedFieldsLog.append("Description|");
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found")));
            updatedFieldsLog.append("Category|");
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            validateEventDate(updateEventAdminRequest.getEventDate());
            event.setEventDate(updateEventAdminRequest.getEventDate());
            updatedFieldsLog.append("EventDate|");
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
            updatedFieldsLog.append("Location|");
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
            updatedFieldsLog.append("Paid|");
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
            updatedFieldsLog.append("ParticipantLimit|");
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
            updatedFieldsLog.append("RequestModeration|");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            updateStateAction(event, updateEventAdminRequest);
            updatedFieldsLog.append("StateAction|");
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
            updatedFieldsLog.append("Annotation|");
        }

        log.info("Updated fields: {}", updatedFieldsLog);
    }

    public void updateEventFromPrivateRequest(Event event, UpdateEventUserRequest updateEventUserRequest, CategoryRepository categoryRepository) {
        StringBuilder updatedFieldsLog = new StringBuilder();

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
            updatedFieldsLog.append("Annotation|");
        }
        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format(CAT_NOT_FOUND_MESSAGE, updateEventUserRequest.getCategory())));

            event.setCategory(category);
            updatedFieldsLog.append("Category|");
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
            updatedFieldsLog.append("Description|");
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
            updatedFieldsLog.append("Location|");
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
            updatedFieldsLog.append("Paid|");
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
            updatedFieldsLog.append("ParticipantLimit|");
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
            updatedFieldsLog.append("RequestModeration|");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            validateEventDate(updateEventUserRequest.getEventDate());
            event.setEventDate(updateEventUserRequest.getEventDate());
            updatedFieldsLog.append("EventDate|");
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
            updatedFieldsLog.append("Title|");
        }
        log.info("Updated fields: {}", updatedFieldsLog);
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (Duration.between(LocalDateTime.now(), eventDate).toHours() < 2) {
            throw new BadRequestException("Event date must be at least 2 hours from now.");
        }
    }

    public LocationDto toLocationDto(Location location) {
        if (location == null) {
            return null;
        }
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    private void updateStateAction(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        StateAdmin stateAction = updateEventAdminRequest.getStateAction();
        if (stateAction == StateAdmin.PUBLISH_EVENT) {
            if (event.getState() == State.PENDING) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                throw new ProblemOfEditingException("Event can only be published if it is in a pending state.");
            }
        } else if (stateAction == StateAdmin.REJECT_EVENT) {
            if (event.getState() == State.PENDING) {
                event.setState(State.CANCELED);
            } else {
                throw new ProblemOfEditingException("Event can only be rejected if it has not yet been published.");
            }
        }
    }
}
