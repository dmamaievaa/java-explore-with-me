package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.enums.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;
import static ru.practicum.ewm.utils.Constants.DEFAULT_TIME_ZONE;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT, timezone = DEFAULT_TIME_ZONE)
    LocalDateTime created;

    Integer event;

    Integer id;

    Integer requester;

    RequestStatus status;
}
