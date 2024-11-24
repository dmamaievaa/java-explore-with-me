package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> get(Integer userId);

    ParticipationRequestDto create(Integer userId, Integer eventId);

    ParticipationRequestDto cancel(Integer userId, Integer requestId);
}
