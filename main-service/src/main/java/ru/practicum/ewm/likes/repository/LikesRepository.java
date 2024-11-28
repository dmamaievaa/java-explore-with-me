package ru.practicum.ewm.likes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.likes.model.Like;
import ru.practicum.ewm.likes.model.LikeId;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like, LikeId> {

    Page<Like> findAllByEventId(Integer eventId, Pageable pageable);

    Page<Like> findAllByUserId(Integer userId, Pageable pageable);

    Page<Like> findAllByEventIdAndUserId(Integer eventId, Integer userId, Pageable pageable);

    Optional<Like> findByEventIdAndUserId(Integer eventId, Integer userId);

    boolean existsByEventIdAndUserId(Integer eventId, Integer userId);
}
