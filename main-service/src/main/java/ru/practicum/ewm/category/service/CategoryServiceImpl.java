package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NotFoundException;


import java.util.List;

import static ru.practicum.ewm.utils.Constants.CAT_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    @Transactional
    public void delete(Integer catId) {
        categoryRepository.delete(findByIdOrThrow(catId));
    }

    @Override
    @Transactional
    public CategoryDto update(Integer catId, CategoryDto categoryDto) {
        Category category = findByIdOrThrow(catId);
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

   @Override
   public List<CategoryDto> get(int offset, int size) {
       if (size <= 0) {
           throw new IllegalArgumentException("Size must be greater than 0");
       }
       Pageable pageable = PageRequest.of(offset / size, size);
       return categoryRepository.findAll(pageable)
               .getContent()
               .stream()
               .map(categoryMapper::toCategoryDto)
               .toList();
   }

    @Override
    public CategoryDto getById(Integer catId) {
        return categoryMapper.toCategoryDto(findByIdOrThrow(catId));
    }

    private Category findByIdOrThrow(Integer catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CAT_NOT_FOUND_MESSAGE + ": " + catId));
    }
}