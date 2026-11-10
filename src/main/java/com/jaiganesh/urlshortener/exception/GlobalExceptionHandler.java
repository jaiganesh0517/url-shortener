package com.jaiganesh.urlshortener.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UrlNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleUrlNotFound(UrlNotFoundException ex){
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", ex.getMessage());
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 404);
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,Object>> handleValidationErrors(MethodArgumentNotValidException ex){
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 400);
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,Object>> handleValidationErrors(Exception ex){
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("message", "Something went wrong");
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 500);
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RateLimitExceededException.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(RateLimitExceededException ex) {
	    Map<String, Object> error = new HashMap<>();
	    error.put("success", false);
	    error.put("message", ex.getMessage());
	    error.put("timestamp", LocalDateTime.now());
	    error.put("status", 429);
	    
	    return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
	}
}
