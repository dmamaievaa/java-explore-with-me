package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.events.enums.StateAdmin;
import ru.practicum.ewm.events.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;
import static ru.practicum.ewm.utils.Constants.DEFAULT_TIME_ZONE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000, message = "min length for annotation field is 20, max length is 2000 characters")
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000, message = "min length for description field is 20, max length is 7000 characters")
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT, timezone = DEFAULT_TIME_ZONE)
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    StateAdmin stateAction;

    @Size(min = 3, max = 120, message = "min length for title field is 3, max length is 120 characters")
    String title;
}
