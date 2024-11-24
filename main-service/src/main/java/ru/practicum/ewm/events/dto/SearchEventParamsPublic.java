package ru.practicum.ewm.events.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchEventParamsPublic {

    String text;

    List<Integer> categories;

    Boolean paid;

    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    LocalDateTime rangeStart;

    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT)
    LocalDateTime rangeEnd;

    Boolean onlyAvailable = false;

    String sort;

    @PositiveOrZero
    Integer from = 0;

    @Positive
    Integer size = 10;
}
