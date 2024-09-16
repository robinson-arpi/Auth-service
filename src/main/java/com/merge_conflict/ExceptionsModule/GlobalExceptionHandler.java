package com.merge_conflict.ExceptionsModule;

import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalEmailArgumentException;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalLoginException;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalNullArgumentException;
import com.merge_conflict.ExceptionsModule.AuthExceptions.IllegalPasswordArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Generally when the client fail, the back return HTTP 400 (Bad Request), but for this case we're using CONFLIT(409)
    @ExceptionHandler(IllegalLoginException.class)
    public ResponseEntity<String> IllegalLoginException(IllegalLoginException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(IllegalEmailArgumentException.class)
    public ResponseEntity<String> IllegalEmailArgumentException(IllegalEmailArgumentException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalPasswordArgumentException.class)
    public ResponseEntity<String> IllegalPasswordArgumentException(IllegalPasswordArgumentException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalNullArgumentException.class)
    public ResponseEntity<String> IllegalNullArgumentException(IllegalNullArgumentException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Map for return errors in the validation
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage()
                ));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        // Crear un mapa para devolver los errores de validación
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));
        // Puedes optar por devolver 409 si prefieres señalar un conflicto
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }
}
