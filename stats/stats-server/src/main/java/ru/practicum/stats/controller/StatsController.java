package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.exception.InvalidParamException;
import ru.practicum.stats.service.StatsService;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.GetStatsRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void hit(@RequestBody HitDto hitDto) {
        service.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<GetStatsResponseDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(defaultValue = "") List<String> uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        if (end.isBefore(start)) {
            throw new InvalidParamException("end", "Uncorrected format of dates");
        }
        return service.getStatsViewList(
                GetStatsRequestDto.builder()
                        .start(start)
                        .end(end)
                        .uris(uris)
                        .unique(unique)
                        .build());
    }
}