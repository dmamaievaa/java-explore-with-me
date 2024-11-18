package ru.practicum.stats.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.model.HitEndpoint;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StatsMapper implements RowMapper<GetStatsResponseDto> {

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
}
