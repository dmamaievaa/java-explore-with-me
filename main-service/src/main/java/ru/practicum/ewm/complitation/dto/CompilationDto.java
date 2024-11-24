package ru.practicum.ewm.complitation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.events.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    Integer id;

    boolean pinned;

    String title;

    List<EventShortDto> events;
}
