package ru.practicum.ewm.complitation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {

    List<Integer> events;

    @Builder.Default
    boolean pinned = false;

    @NotBlank
    @Size(min = 1, max = 50, message = "min length for title field is 1, max length is 50 characters")
    String title;
}
