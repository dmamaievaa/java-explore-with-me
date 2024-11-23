package ru.practicum.ewm.complitation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.complitation.dto.CompilationDto;
import ru.practicum.ewm.complitation.dto.NewCompilationDto;
import ru.practicum.ewm.complitation.model.Compilation;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.model.Event;

import java.util.List;
import java.util.Set;

@Component
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .events(events)
                .pinned(newCompilationDto.isPinned())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(events)
                .build();
    }
}
