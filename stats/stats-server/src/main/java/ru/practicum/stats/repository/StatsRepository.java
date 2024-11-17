package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.stats.model.HitEndpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<HitEndpoint, Long> {

    @Query("SELECT new ru.practicum.dto.GetStatsResponseDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM HitEndpoint h WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<GetStatsResponseDto> getStats(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.GetStatsResponseDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM HitEndpoint h WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<GetStatsResponseDto> getUniqueStats(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") List<String> uris);
}
