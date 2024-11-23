package ru.practicum.stats.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.exception.BadRequestException;
import ru.practicum.stats.exception.InvalidParamException;
import ru.practicum.stats.service.StatsService;
import ru.practicum.dto.GetStatsResponseDto;
import ru.practicum.dto.GetStatsRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void hit(@RequestBody HitDto hitDto) {
        service.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<GetStatsResponseDto> getStats(@Valid GetStatsRequestDto getStatsRequestDto) {
        if (getStatsRequestDto.getStart() == null || getStatsRequestDto.getEnd() == null) {
            throw new BadRequestException("start and end dates are mandatory params");
        }

        if (getStatsRequestDto.getEnd().isBefore(getStatsRequestDto.getStart())) {
            throw new InvalidParamException("end", "Uncorrected format of dates");
        }

        return service.getStatsViewList(getStatsRequestDto);
    }
}