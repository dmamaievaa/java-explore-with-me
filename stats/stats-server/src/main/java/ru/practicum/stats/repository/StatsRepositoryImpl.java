/*package ru.practicum.stats.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.dto.GetStatsRequestDto;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.stats.mapper.StatsMapper;
import ru.practicum.stats.model.HitEndpoint;

import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsRepositoryImpl implements StatsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StatsMapper statsMapper;

    @Override
    public List<GetStatsResponseDto> getUniqueStats(GetStatsRequestDto request) {
        String query = "SELECT app, uri, COUNT (DISTINCT ip) AS hits FROM stats WHERE (created >= ? AND created <= ?) ";
        if (!request.getUris().isEmpty()) {
            query += createUrisQuery(request.getUris());
        }
        query += " GROUP BY app, uri ORDER BY hits DESC";
        return jdbcTemplate.query(query, statsMapper::mapRow, request.getStart(), request.getEnd());
    }

    @Override
    public List<GetStatsResponseDto> getStats(GetStatsRequestDto request) {
        String query = "SELECT app, uri, COUNT (ip) AS hits FROM stats WHERE (created >= ? AND created <= ?) ";
        if (!request.getUris().isEmpty()) {
            query += createUrisQuery(request.getUris());
        }
        query += " GROUP BY app, uri ORDER BY hits DESC";
        return jdbcTemplate.query(query, statsMapper::mapRow, request.getStart(), request.getEnd());
    }

    @Override
    public void saveHit(HitEndpoint hitEndpoint) {
        jdbcTemplate.update("INSERT INTO stats (app, uri, ip, created) VALUES (?, ?, ?, ?)",
                hitEndpoint.getApp(), hitEndpoint.getUri(), hitEndpoint.getIp(), Timestamp.valueOf(hitEndpoint.getTimestamp()));
    }

    private String createUrisQuery(List<String> uris) {
        return "AND uri IN ('" + String.join("', '", uris) +
                "') ";
    }
}
*/