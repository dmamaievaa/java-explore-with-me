package ru.practicum.stats.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.model.HitEndpoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class StatsMapper {

    public HitEndpoint toEndpointHit(HitDto dto) {
        return HitEndpoint.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public GetStatsResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GetStatsResponseDto.builder()
                .app(rs.getString("app"))
                .uri(rs.getString("uri"))
                .hits(rs.getLong("hits"))
                .build();
    }

    public List<GetStatsResponseDto> toViewStatsDtoList(Collection<HitEndpoint> hits) {
        Map<String, Map<String, Long>> groupedStats = hits.stream()
                .collect(Collectors.groupingBy(HitEndpoint::getApp,
                        Collectors.groupingBy(HitEndpoint::getUri, Collectors.counting())));

        return groupedStats.entrySet().stream()
                .flatMap(appEntry -> appEntry.getValue().entrySet().stream()
                        .map(uriEntry -> GetStatsResponseDto.builder()
                                .app(appEntry.getKey())
                                .uri(uriEntry.getKey())
                                .hits(uriEntry.getValue())
                                .build()))
                .sorted((dto1, dto2) -> Long.compare(dto2.getHits(), dto1.getHits()))
                .toList();
    }
}
