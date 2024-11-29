package ru.practicum.ewm.events.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.SearchEventParamsPublic;
import ru.practicum.ewm.events.service.EventService;

import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> get(@Valid SearchEventParamsPublic searchEventPublicParams,
                                               HttpServletRequest request) {
        return eventService.getFromPublic(searchEventPublicParams, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable("eventId") Integer eventId, HttpServletRequest request) {
        return eventService.getById(eventId, request);
    }

    @GetMapping("/top-rated")
    public List<EventShortDto> getTopRatedEvents(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        return eventService.getTopRatedEvents(limit, request);
    }
}
