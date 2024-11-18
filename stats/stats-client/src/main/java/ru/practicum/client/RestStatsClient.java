package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Service
public class RestStatsClient extends BaseClient implements StatsClient {

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RestStatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build());
    }

    @Override
    public void postStats(HitDto endpointHitDto) {
        try {
            log.info("Sending POST request to /hit with data: {}", endpointHitDto);
            post("/hit", endpointHitDto);
            log.info("POST request to /hit was successful");
        } catch (Exception e) {
            log.error("Error executing postStats: {}", e.getMessage(), e);
            throw new RuntimeException("Error sending statistics", e);
        }
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DEFAULT_DATE_FORMATTER),
                "end", end.format(DEFAULT_DATE_FORMATTER),
                "uris", String.join(",", uris),
                "unique", unique
        );

        try {
            return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } catch (Exception e) {
            log.error("Error executing getStats: {}", e.getMessage(), e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}

