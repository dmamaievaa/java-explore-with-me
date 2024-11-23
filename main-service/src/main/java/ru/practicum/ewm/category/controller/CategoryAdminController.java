package ru.practicum.ewm.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.create(newCategoryDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("categoryId") Integer catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto update(@PathVariable("categoryId") Integer catId, @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.update(catId, categoryDto);
    }
}
