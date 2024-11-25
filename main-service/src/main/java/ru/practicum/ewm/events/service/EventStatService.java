package ru.practicum.ewm.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.events.model.Event;

public interface EventStatService {

    void sendStatData(HttpServletRequest request);

    Integer getUniqueViews(Event event, String uri);
}
