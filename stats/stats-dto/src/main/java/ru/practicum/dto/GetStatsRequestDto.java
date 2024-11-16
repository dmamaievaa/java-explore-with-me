package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class GetStatsRequestDto {

    @Builder.Default
    private LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

    @Builder.Default
    private LocalDateTime end = LocalDateTime.now();

    private List<String> uris;

    private boolean unique;
}