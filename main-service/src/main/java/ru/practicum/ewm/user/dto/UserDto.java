package ru.practicum.ewm.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Integer id;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 250, message = "min length for name field is 2, max length is 250 characters")
    String name;

    @NotBlank
    @NotNull
    @NotEmpty
    @Email(message = "Incorrect format")
    @Size(min = 6, max = 254, message = "min length for email field is 6, max length is 254 characters")
    String email;
}
