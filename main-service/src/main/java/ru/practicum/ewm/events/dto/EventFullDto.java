package ru.practicum.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.events.enums.State;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;
import static ru.practicum.ewm.utils.Constants.DEFAULT_TIME_ZONE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    Integer id;

    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
   LocalDateTime createdOn;

    String description;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    LocalDateTime eventDate;

    UserShortDto initiator;

    LocationDto location;

    @NotBlank
    boolean paid;

    @Builder.Default
    Integer participantLimit = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT, timezone = DEFAULT_TIME_ZONE)
    LocalDateTime publishedOn;

    @Builder.Default
    boolean requestModeration = true;

    State state;

    @NotBlank
    String title;

    Integer views;

    Integer likes;

    Integer dislikes;

    Float rating;

    @Builder.Default
    boolean likesHidden = false;
}
