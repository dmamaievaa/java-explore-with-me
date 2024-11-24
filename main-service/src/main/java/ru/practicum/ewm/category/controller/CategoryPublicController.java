package ru.practicum.ewm.category.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.DEFAULT_FROM;
import static ru.practicum.ewm.utils.Constants.DEFAULT_SIZE;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> get(
            @RequestParam(defaultValue = DEFAULT_FROM + "") @Min(0) int from,
            @RequestParam(defaultValue = DEFAULT_SIZE + "") @Min(1) int size) {
        return categoryService.get(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getById(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getById(categoryId);
    }
}
