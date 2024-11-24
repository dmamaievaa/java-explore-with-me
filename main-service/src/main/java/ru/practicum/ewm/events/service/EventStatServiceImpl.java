package ru.practicum.ewm.events.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.HitDto;
import ru.practicum.ewm.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventStatServiceImpl implements EventStatService {

    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;


    @Value("${spring.application.name}")
    private String serviceId;

    @Override
    public void sendStatData(HttpServletRequest request) {
        HitDto stat = HitDto.builder()
                .app(serviceId)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        statsClient.create(stat);
    }

    @Override
    public Integer getUniqueViews(Event event, String uri) {

        LocalDateTime startDate = event.getCreatedOn();
        LocalDateTime endDate = LocalDateTime.now();

        List<GetStatsResponseDto> stats = Optional.ofNullable(statsClient.getStats(startDate, endDate, List.of(uri), true).getBody())
                .map(body -> {
                    try {
                        return objectMapper.convertValue(body, new TypeReference<List<GetStatsResponseDto>>() {});
                    } catch (Exception e) {
                        throw new IllegalStateException("Failed to convert response to list", e);
                    }
                })
                .orElse(List.of());

        return stats.stream()
                .mapToLong(GetStatsResponseDto::getHits)
                .sum() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) stats.stream()
                .mapToLong(GetStatsResponseDto::getHits)
                .sum();
    }
}
