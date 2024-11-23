package ru.practicum.ewm.complitation.service;

import ru.practicum.ewm.complitation.dto.CompilationDto;
import ru.practicum.ewm.complitation.dto.NewCompilationDto;
import ru.practicum.ewm.complitation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(Integer compId);

    CompilationDto create(NewCompilationDto newCompilationDto);

    void deleteById(Integer compId);

    CompilationDto update(Integer compId, UpdateCompilationRequest updateCompilationRequest);
}
