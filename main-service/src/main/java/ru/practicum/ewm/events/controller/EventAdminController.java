package ru.practicum.ewm.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.SearchEventParamsAdmin;
import ru.practicum.ewm.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.events.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> get(@Valid SearchEventParamsAdmin searchEventParamsAdmin) {
        return eventService.getFromAdmin(searchEventParamsAdmin);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("eventId") Integer eventId,
                                      @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateByAdmin(eventId, updateEventAdminRequest);
    }

    @PatchMapping("/{eventId}/hide-likes")
    public EventFullDto hideLikes(@PathVariable("eventId") Integer eventId,
                                  @RequestParam Boolean hideLikes) {
        return eventService.updateLikesVisibility(eventId, hideLikes);
    }
}
