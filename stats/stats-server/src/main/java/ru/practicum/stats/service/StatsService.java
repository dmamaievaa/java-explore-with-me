package ru.practicum.stats.service;

import ru.practicum.dto.GetStatsRequestDto;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.HitDto;

import java.util.List;

public interface StatsService {

    void createHit(HitDto endpointHitDto);

    List<GetStatsResponseDto> getStatsViewList(GetStatsRequestDto statsRequestDto);
}
