package org.urbanclimatemonitor.backend.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

import java.util.*;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice
{
	@Data
	@AllArgsConstructor
	private static class ErrorResponse
	{
		private int status;
		private String message;
		private List<String> errors;
	}

	private final MessageSource messageSource;

	public ExceptionHandlerControllerAdvice(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	private static ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String message, List<String> errors)
	{
		return new ResponseEntity<>(Map.of("error", new ErrorResponse(status.value(), message, errors)), status);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e, Locale locale)
	{
		e.printStackTrace();
		String message = messageSource.getMessage("internal-server-error", null, locale);
		return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
	}

	@ExceptionHandler({ CustomLocalizedException.class })
	public ResponseEntity<Map<String, Object>> localizedExceptionHandler(CustomLocalizedException e, Locale locale)
	{
		String message = messageSource.getMessage(e.getId(), e.getArguments(), locale);
		return buildResponseEntity(e.getHttpStatus(), message, null);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, Locale locale)
	{
		List<String> errors = new LinkedList<>();

		BindingResult bindingResult = e.getBindingResult();
		for (ObjectError objectError : bindingResult.getAllErrors()) {
			errors.add(messageSource.getMessage(Objects.requireNonNull(objectError.getDefaultMessage()), null, locale));
		}

		String message = messageSource.getMessage("argument-not-valid", null, locale);
		return buildResponseEntity(HttpStatus.BAD_REQUEST, message, errors);
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Map<String, Object>> accessDeniedExceptionHandler(AccessDeniedException e, Locale locale)
	{
		String message = messageSource.getMessage("access-denied", null, locale);
		return buildResponseEntity(HttpStatus.FORBIDDEN, message, null);
	}

	@ExceptionHandler({ InsufficientAuthenticationException.class })
	public ResponseEntity<Map<String, Object>> insufficientAuthenticationExceptionHandler(InsufficientAuthenticationException e, Locale locale)
	{
		String message = messageSource.getMessage("access-denied", null, locale);
		return buildResponseEntity(HttpStatus.FORBIDDEN, message, null);
	}
}
