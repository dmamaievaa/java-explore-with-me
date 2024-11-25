package ru.practicum.ewm.complitation.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.complitation.dto.CompilationDto;
import ru.practicum.ewm.complitation.service.CompilationService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.DEFAULT_FROM;
import static ru.practicum.ewm.utils.Constants.DEFAULT_SIZE;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam (defaultValue = DEFAULT_FROM + "") @Min(0) int from,
                                                @RequestParam (defaultValue = DEFAULT_SIZE + "") @Min(1) int size) {
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        return compilationService.getById(compId);
    }
}
