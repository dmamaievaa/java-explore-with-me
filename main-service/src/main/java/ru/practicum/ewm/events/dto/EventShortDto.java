package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;
import static ru.practicum.ewm.utils.Constants.DEFAULT_TIME_ZONE;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {

    Integer id;

    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT, timezone = DEFAULT_TIME_ZONE)
    LocalDateTime eventDate;

    UserShortDto initiator;

    boolean paid;

    String title;

    Integer views;

    Integer likes;

    Integer dislikes;

    Float rating;

    @Builder.Default
    boolean likesHidden = false;
}
