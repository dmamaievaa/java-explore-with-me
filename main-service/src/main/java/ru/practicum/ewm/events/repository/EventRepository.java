package ru.practicum.ewm.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> findEventByIdAndInitiator(Integer eventId, User initiator);

    Optional<Event> findByIdAndState(Integer eventId, State state);

    Page<Event> findAllByState(State state, Pageable pageable);
}
