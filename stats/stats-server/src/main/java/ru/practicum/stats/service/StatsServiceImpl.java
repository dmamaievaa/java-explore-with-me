package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.GetStatsRequestDto;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.mapper.StatsMapper;
import ru.practicum.stats.model.HitEndpoint;
import ru.practicum.stats.repository.StatsRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    @Transactional
    public void createHit(HitDto endpointHitDto) {
        HitEndpoint endpointHit = statsMapper.toEndpointHit(endpointHitDto);
        statsRepository.save(endpointHit);
    }

    @Override
    public List<GetStatsResponseDto> getStatsViewList(GetStatsRequestDto statsRequestDto) {
        List<String> uris = statsRequestDto.getUris().isEmpty() ? null : statsRequestDto.getUris();

        return statsRequestDto.isUnique() ?
                statsRepository.getUniqueStats(statsRequestDto.getStart(), statsRequestDto.getEnd(), uris) :
                statsRepository.getStats(statsRequestDto.getStart(), statsRequestDto.getEnd(), uris);
    }
}
