package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {

    void postStats(HitDto endpointHitDto);

    ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
