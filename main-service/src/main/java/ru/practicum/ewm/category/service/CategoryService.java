package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(Integer id);

    CategoryDto update(Integer catId, CategoryDto categoryDto);

    List<CategoryDto> get(int from, int size);

    CategoryDto getById(Integer catId);
}