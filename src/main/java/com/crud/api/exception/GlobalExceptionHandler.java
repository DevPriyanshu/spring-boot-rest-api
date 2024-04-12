package com.crud.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> resourceNotFoundException(
          ResourceNotFoundException ex, WebRequest request) {
    logger.log(Level.SEVERE, "ResourceNotFoundException occurred: " + ex.getMessage(), ex);
    ErrorResponse errorDetails =
            new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
    logger.log(Level.SEVERE, "Exception occurred: " + ex.getMessage(), ex);
    ErrorResponse errorDetails =
            new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> validationException(ValidationException ex, WebRequest request) {
    logger.log(Level.SEVERE, "ValidationException occurred: " + ex.getMessage(), ex);
    ErrorResponse errorDetails =
            new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnAuthenticatedException.class)
  public ResponseEntity<?> unAuthenticatedException(
          UnAuthenticatedException ex, WebRequest request) {
    logger.log(Level.SEVERE, "UnAuthenticatedException occurred: " + ex.getMessage(), ex);
    ErrorResponse errorDetails =
            new ErrorResponse(new Date(), HttpStatus.FORBIDDEN.toString(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }
}
