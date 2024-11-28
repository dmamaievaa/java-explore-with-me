package ru.practicum.ewm.likes.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.likes.model.Like;
import ru.practicum.ewm.likes.model.LikeId;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like, LikeId> {

    List<Like> findAllByEventId(Integer eventId, Pageable pageable);

    List<Like> findAllByUserId(Integer userId, Pageable pageable);

    List<Like> findAllByEventIdAndUserId(Integer eventId, Integer userId, Pageable pageable);

    Optional<Like> findByEventIdAndUserId(Integer eventId, Integer userId);

    boolean existsByEventIdAndUserId(Integer eventId, Integer userId);
}