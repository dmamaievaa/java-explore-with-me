package ru.practicum.ewm.complitation.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {

    List<Integer> events;

    Boolean pinned;

    @Size(min = 1, max = 50, message = "min length for title field is 1, max length is 50 characters")
    String title;
}
