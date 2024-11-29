package ru.practicum.ewm.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.events.dto.EventFullDto;
import ru.practicum.ewm.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.dto.NewEventDto;
import ru.practicum.ewm.events.dto.SearchEventParamsPublic;
import ru.practicum.ewm.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.events.dto.SearchEventParamsAdmin;


import java.util.List;

public interface EventService {

    List<EventFullDto> getFromAdmin(SearchEventParamsAdmin searchEventParamsAdmin);

    EventFullDto updateByAdmin(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

   List<EventShortDto> getFromPublic(SearchEventParamsPublic searchEventPublicParams, HttpServletRequest request);

    EventFullDto getById(Integer eventId, HttpServletRequest request);

    List<EventShortDto> getByCurrentUser(Integer userId, int from, int size);

    EventFullDto addFromPrivate(Integer userId, NewEventDto newEventDto);

    EventFullDto getFullByCurrentUser(Integer userId, Integer eventId);

    EventFullDto updateByCurrentUser(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequestsByCurrentUser(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateStatus(Integer userId,
                                                Integer eventId,
                                                EventRequestStatusUpdateRequest statusUpdateRequest);

    void increaseLikes(Integer eventId);

    void reduceLikes(Integer eventId);

    void increaseDislikes(Integer eventId);

    void reduceDislikes(Integer eventId);

    void updateEventRatingValue(Integer eventId);

    List<EventShortDto> getTopRatedEvents(int limit, HttpServletRequest request);

    EventFullDto updateLikesVisibility(Integer eventId, Boolean hideLikes);

}
