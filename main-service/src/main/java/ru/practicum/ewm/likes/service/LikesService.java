package ru.practicum.ewm.likes.service;

import ru.practicum.ewm.likes.model.Like;

import java.util.List;

public interface LikesService {

    void addLike(Integer eventId, Integer userId);

    void removeLike(Integer eventId, Integer userId);

    void addDislike(Integer eventId, Integer userId);

    void removeDislike(Integer eventId, Integer userId);

    List<Like> search(Integer eventId, Integer userId, int from, int size);
}
