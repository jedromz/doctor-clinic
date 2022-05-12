package com.jedromz.doctorclinic.error;

import com.jedromz.doctorclinic.error.constraint.ConstraintErrorHandler;
import lombok.Value;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Map<String, ConstraintErrorHandler> constraintsMap;

    public GlobalExceptionHandler(Set<ConstraintErrorHandler> handlers) {
        this.constraintsMap = handlers.stream()
                .collect(Collectors.toMap(ConstraintErrorHandler::constraintName, Function.identity()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        return new ResponseEntity(exc.getFieldErrors().stream().map(fe -> new ValidationErrorDto(fe.getDefaultMessage(), fe.getField())).collect(toList()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException exc) {
        return new ResponseEntity(new NotFoundDto(exc.getName(), exc.getKey()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity handleEntityNotFoundException(ObjectOptimisticLockingFailureException exc) {
        return new ResponseEntity(new OptimisticLockDto("OPTIMISTIC_LOCK"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateConflictException.class)
    public ResponseEntity handleDateConflictException(DateConflictException exc) {
        return new ResponseEntity(new DateConflictExceptionDto(exc.getDate(), exc.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException exc) {
        String constraintName = exc.getConstraintName().substring(1, exc.getConstraintName().indexOf(" "));
        String message = constraintsMap.get(constraintName).message();
        String field = constraintsMap.get(constraintName).field();
        return new ResponseEntity(new ValidationErrorDto(message, field), HttpStatus.BAD_REQUEST);
    }

    @Value
    class ValidationErrorDto {
        private String message;
        private String field;
    }

    @Value
    class NotFoundDto {
        private String entityName;
        private String entityKey;
    }

    @Value
    class DateConflictExceptionDto {
        private String date;
        private String message;
    }

    @Value
    class OptimisticLockDto {
        private String message;
    }
}
