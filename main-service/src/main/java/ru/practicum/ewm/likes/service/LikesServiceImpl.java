package ru.practicum.ewm.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.repository.EventRepository;
import ru.practicum.ewm.events.service.EventService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ProblemOfEditingException;
import ru.practicum.ewm.likes.model.Like;
import ru.practicum.ewm.likes.repository.LikesRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.EVENT_NOT_FOUND_MESSAGE;
import static ru.practicum.ewm.utils.Constants.LIKE_NOT_FOUND_MESSAGE;
import static ru.practicum.ewm.utils.Constants.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesServiceImpl implements LikesService {

    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Override
    public void addLike(Integer eventId, Integer userId) {
        Event event = findPublishedEventById(eventId);
        User user = findUserById(userId);

        if (likesRepository.existsByEventIdAndUserId(eventId, userId) && isDislike(eventId, userId)) {
            removeDislike(eventId, userId);
        }

        if (likesRepository.existsByEventIdAndUserId(eventId, userId) && isLike(eventId, userId)) {
            throw new ProblemOfEditingException(String.format("User with ID %d already liked event with ID %d", userId, eventId));
        }

        Like like = Like.builder()
                .userId(userId)
                .eventId(eventId)
                .build();
        likesRepository.save(like);

        eventService.increaseLikes(eventId);
    }

    @Override
    public void removeLike(Integer eventId, Integer userId) {
        Like like = findLikeByEventAndUser(eventId, userId);

        if (!likesRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new ProblemOfEditingException(String.format("User with ID %d didn't like event with ID %d", userId, eventId));
        }

        likesRepository.delete(like);
        eventService.reduceLikes(eventId);
    }

    @Override
    public void addDislike(Integer eventId, Integer userId) {
        Event event = findPublishedEventById(eventId);
        User user = findUserById(userId);

        if (likesRepository.existsByEventIdAndUserId(eventId, userId) && isLike(eventId, userId)) {
            removeLike(eventId, userId);
        }

        if (likesRepository.existsByEventIdAndUserId(eventId, userId) && isDislike(eventId, userId)) {
            throw new ProblemOfEditingException(String.format("User with ID %d already disliked event with ID %d", userId, eventId));
        }

        Like dislike = Like.builder()
                .userId(userId)
                .eventId(eventId)
                .build();

        likesRepository.save(dislike);

        eventService.increaseDislikes(eventId);
    }

    @Override
    public void removeDislike(Integer eventId, Integer userId) {
        Like dislike = findLikeByEventAndUser(eventId, userId);

        if (!likesRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new ProblemOfEditingException(String.format("User with ID %d didn't dislike event with ID %d", userId, eventId));
        }

        likesRepository.delete(dislike);
        eventService.reduceDislikes(eventId);
    }

    @Override
    public List<Like> search(Integer eventId, Integer userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (eventId != null && userId != null) {
            return likesRepository.findAllByEventIdAndUserId(eventId, userId, pageable).getContent();
        } else if (eventId != null) {
            return likesRepository.findAllByEventId(eventId, pageable).getContent();
        } else if (userId != null) {
            return likesRepository.findAllByUserId(userId, pageable).getContent();
        } else {
            return likesRepository.findAll(pageable).getContent();
        }
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
    }

    private Like findLikeByEventAndUser(Integer eventId, Integer userId) {
        return likesRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format(LIKE_NOT_FOUND_MESSAGE, eventId, userId)));
    }

    private Event findPublishedEventById(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MESSAGE, eventId)));

        if (event.getState() != State.PUBLISHED) {
            throw new ProblemOfEditingException(String.format("Event with ID %d is not published", eventId));
        }

        return event;
    }

    private boolean isLike(Integer eventId, Integer userId) {
        Like like = likesRepository.findByEventIdAndUserId(eventId, userId).orElse(null);
        return like != null;
    }

    private boolean isDislike(Integer eventId, Integer userId) {
        Like dislike = likesRepository.findByEventIdAndUserId(eventId, userId).orElse(null);
        return dislike != null;
    }
}
