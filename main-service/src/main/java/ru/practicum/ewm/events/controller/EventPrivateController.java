package ru.practicum.ewm.events.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.NewEventDto;
import ru.practicum.ewm.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.events.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.DEFAULT_FROM;
import static ru.practicum.ewm.utils.Constants.DEFAULT_SIZE;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> get(@PathVariable("userId") Integer userId,
                                   @RequestParam(defaultValue = DEFAULT_FROM + "") int from,
                                   @RequestParam(defaultValue = DEFAULT_SIZE + "") @Min(1) int size) {
        return eventService.getByCurrentUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable("userId") Integer userId,
                               @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.addFromPrivate(userId, newEventDto);
    }


    @GetMapping("/{eventId}")
    public EventFullDto getFull(@PathVariable("userId") Integer userId,
                                                   @PathVariable("eventId") Integer eventId) {
        return eventService.getFullByCurrentUser(userId, eventId);
    }


    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("userId") Integer userId,
                                            @PathVariable("eventId") Integer eventId,
                                            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateByCurrentUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable("userId") Integer userId,
                                                                  @PathVariable("eventId") Integer eventId) {

        return eventService.getRequestsByCurrentUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable("userId") Integer userId,
                                                       @PathVariable("eventId") Integer eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        return eventService.updateStatus(userId, eventId, statusUpdateRequest);
    }

}
