package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.user.model.User;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findByEvent(Event event);

    Collection<ParticipationRequest> findByRequester(User user);

    Collection<ParticipationRequest> findByEventAndStatus(Event event, RequestStatus status);

    Integer countByEventAndStatus(Event event, RequestStatus status);
}
