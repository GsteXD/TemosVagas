package br.fatec.TemosVagas.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Tratamento global de exceções, impede que o Spring Security intercepte no envio de erros para rotas com .permitAll()
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorsDTO> handleAll(
                Exception ex) {

                ErrorsDTO error = new ErrorsDTO(
                        "Erro interno: " + ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                );

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        // Tratamento de exceções do @Valid
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorsDTO> handleValidationExceptions(
                MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

                ErrorsDTO error = new ErrorsDTO(
                        errors,
                        HttpStatus.BAD_REQUEST.value()
                );

                return ResponseEntity.badRequest().body(error);
        }

        // Tratamento de exceções de entidades não encontradas
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorsDTO> handleEntityNotFoundException(
                EntityNotFoundException ex) {

                ErrorsDTO error = new ErrorsDTO(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Tratamento de exceções de rotas http incorretas
        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorsDTO> handleNoResourceFoundException(
                NoResourceFoundException ex) {

                ErrorsDTO error = new ErrorsDTO(
                        "Rota não encontrada: " + ex.getResourcePath(),
                        HttpStatus.NOT_FOUND.value()
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

}
