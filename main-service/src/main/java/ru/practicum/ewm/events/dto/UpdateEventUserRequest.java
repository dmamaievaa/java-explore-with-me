package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ru.practicum.ewm.events.model.Location;
import ru.practicum.ewm.events.enums.UserState;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;

@Getter
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000, message = "min length for annotation field is 20, max length is 2000 characters")
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000, message = "min length for description field is 20, max length is 7000 characters")
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;

    UserState stateAction;

    @Size(min = 3, max = 120, message = "min length for title field is 3, max length is 120 characters")
    String title;
}
