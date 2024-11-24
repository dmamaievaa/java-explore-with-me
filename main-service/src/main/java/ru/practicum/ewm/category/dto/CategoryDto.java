package ru.practicum.ewm.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {

    Integer id;

    @NotBlank
    @Size(min = 1, max = 50, message = "min length for name field is 1, max length is 50 characters")
    String name;
}
