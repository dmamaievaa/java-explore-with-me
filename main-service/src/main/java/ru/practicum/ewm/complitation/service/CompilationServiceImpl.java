package ru.practicum.ewm.complitation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.complitation.dto.CompilationDto;
import ru.practicum.ewm.complitation.dto.NewCompilationDto;
import ru.practicum.ewm.complitation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.complitation.mapper.CompilationMapper;
import ru.practicum.ewm.complitation.model.Compilation;
import ru.practicum.ewm.complitation.repository.CompilationRepository;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.mapper.EventMapper;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.events.repository.EventRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.practicum.ewm.utils.Constants.COMPILATION_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Compilation> compilations = (pinned != null)
                ? compilationRepository.findAllByPinned(pinned, pageable)
                : compilationRepository.findAll(pageable).toList();

        return compilations.stream()
                .map(compilation -> compilationMapper.toCompilationDto(compilation, mapEventsToShortDtos(compilation.getEvents())))
                .toList();
    }

    @Override
    public CompilationDto getById(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND_MESSAGE, compId)));

        return compilationMapper.toCompilationDto(compilation, mapEventsToShortDtos(compilation.getEvents()));
    }

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {

        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null) {
            events.addAll(eventRepository.findAllById(newCompilationDto.getEvents()));
        }
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilation = compilationRepository.save(compilation);

        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(eventMapper::toShortDto)
                .toList();

        return compilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public void deleteById(Integer compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(String.format(COMPILATION_NOT_FOUND_MESSAGE, compId));
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto update(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND_MESSAGE, compId)));

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        compilation = compilationRepository.save(compilation);

        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(eventMapper::toShortDto)
                .toList();

        return compilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    private List<EventShortDto> mapEventsToShortDtos(Set<Event> events) {
        return events.stream()
                .map(eventMapper::toShortDto)
                .toList();
    }
}
