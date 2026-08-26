package com.hms.api.login;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

record ErrorCapsule(int status, String error, String message, String path, LocalDateTime timestamp) {
	ErrorCapsule(int status, String error, String message, String path) {
		this(status, error, message, path, LocalDateTime.now());
	}
}

@RestControllerAdvice
public class CustomRestControllerAdvice {
	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<ErrorCapsule> handleInsufficientAuthenticationException(
			InsufficientAuthenticationException ex, HttpServletRequest req) {
		ErrorCapsule error = new ErrorCapsule(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(),
				ex.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorCapsule> handleBadCredentialsException(BadCredentialsException ex,
			HttpServletRequest req) {
		ErrorCapsule error = new ErrorCapsule(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(),
				ex.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorCapsule> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
		ErrorCapsule error = new ErrorCapsule(HttpStatus.METHOD_NOT_ALLOWED.value(),
				HttpStatus.METHOD_NOT_ALLOWED.name(), ex.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorCapsule> handleAllOtherExceptions(Exception ex, HttpServletRequest req) {
		ErrorCapsule error = new ErrorCapsule(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
				ex.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

}
