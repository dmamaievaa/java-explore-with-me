package ru.practicum.ewm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constants.DEFAULT_DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
public class ApiError {

    HttpStatus status;

    String reason;

    String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    LocalDateTime timestamp = LocalDateTime.now();
}