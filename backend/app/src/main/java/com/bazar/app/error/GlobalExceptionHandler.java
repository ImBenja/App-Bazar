package com.bazar.app.error;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  // =========================
  // 404 - Recurso no encontrado
  // =========================
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
  }

  // =========================
  // 422 - Reglas de negocio
  // =========================
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(), LocalDateTime.now()));
  }

  // =========================
  // 400 - Validaciones de DTO (@Valid)
  // =========================
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();

    return ResponseEntity.badRequest()
        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Errores de validación", LocalDateTime.now(), errors));
  }

  // =========================
  // 400 - Parámetros mal formados / tipos incorrectos
  // =========================
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String msg = "Parámetro inválido: " + ex.getName() + " = " + ex.getValue();
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), msg, LocalDateTime.now()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
    String msg = "Falta el parámetro obligatorio: " + ex.getParameterName();
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), msg, LocalDateTime.now()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonMalFormado(HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "JSON mal formado o cuerpo vacío", LocalDateTime.now()));
  }

  // =========================
  // 405 - Método HTTP incorrecto
  // =========================
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
    String msg = "Método HTTP no permitido. Usá: " + String.join(", ", ex.getSupportedMethods());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), msg, LocalDateTime.now()));
  }

  // =========================
  // 409 - Conflictos de datos (DB)
  // =========================
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflicto de datos (posible duplicado o FK)",
            LocalDateTime.now()));
  }

  // =========================
  // 400 - Errores de argumentos / estado inválido
  // =========================
  @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class,
      UnsupportedOperationException.class })
  public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now()));
  }

  // =========================
  // 500 - Errores inesperados
  // =========================
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor",
            LocalDateTime.now()));
  }
}
