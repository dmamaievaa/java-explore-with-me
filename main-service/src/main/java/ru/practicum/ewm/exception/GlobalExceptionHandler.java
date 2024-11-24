package ru.practicum.ewm.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        return createErrorResponse(HttpStatus.BAD_REQUEST, String.format("Required request parameter '%s' is not present", parameterName), ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "The required object was not found.", ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Incorrectly formed request.", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "Field: " + error.getField() + ". Error: " + error.getDefaultMessage() + ". Value: " + error.getRejectedValue())
                .toList();
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", String.join("; ", errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = "Failed to convert value of type " + Objects.requireNonNull(ex.getValue()).getClass().getSimpleName() +
                " to required type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() +
                "; nested exception is " + ex.getCause().getMessage();
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Incorrectly formed request.", message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return createErrorResponse(HttpStatus.CONFLICT, "Integrity constraint has been violated.", message);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, "A conflict occurred.", ex.getMessage());
    }

    @ExceptionHandler(ProblemOfEditingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleViolationOfEditingRulesException(ProblemOfEditingException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, "For the requested operation the conditions are not met.", ex.getMessage());
    }

    @ExceptionHandler(EventParticipationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventParticipationConstraintException(EventParticipationException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, "Restriction of participation in the event.", ex.getMessage());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        return createErrorResponse(HttpStatus.CONFLICT, "Integrity constraint has been violated.", message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred: " + ex.getMessage());
    }

    private ApiError createErrorResponse(HttpStatus status, String reason, String message) {
        return new ApiError(status, reason, message, LocalDateTime.now());
    }
}
