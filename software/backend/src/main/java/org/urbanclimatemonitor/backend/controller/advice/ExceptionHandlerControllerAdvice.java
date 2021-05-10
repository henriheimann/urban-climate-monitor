package org.urbanclimatemonitor.backend.controller.advice;

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
		/**
		 * HttpStatus of the error.
		 */
		private int status;

		/**
		 * The translated message of the error.
		 */
		private String message;

		/**
		 * The key of the error message to be translated in the frontend.
		 */
		private String messageKey;

		/**
		 * The params used during translation of the message.
		 */
		private Map<String, String> messageParams;
	}

	private final MessageSource messageSource;

	public ExceptionHandlerControllerAdvice(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String messageKey, Map<String, String> messageParams, Locale locale)
	{
		ErrorResponse errorResponse = new ErrorResponse(
				status.value(),
				messageSource.getMessage(messageKey, null, locale),
				messageKey,
				messageParams
		);

		return new ResponseEntity<>(Map.of("error", errorResponse), status);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e, Locale locale)
	{
		e.printStackTrace();
		return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error", null, locale);
	}

	@ExceptionHandler({ CustomLocalizedException.class })
	public ResponseEntity<Map<String, Object>> localizedExceptionHandler(CustomLocalizedException e, Locale locale)
	{
		return buildResponseEntity(e.getHttpStatus(), e.getId(), e.getParams(), locale);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, Locale locale)
	{
		List<String> errors = new LinkedList<>();

		BindingResult bindingResult = e.getBindingResult();
		for (ObjectError objectError : bindingResult.getAllErrors()) {
			errors.add(messageSource.getMessage(Objects.requireNonNull(objectError.getDefaultMessage()), null, locale));
		}

		return buildResponseEntity(HttpStatus.BAD_REQUEST, "argument_not_valid", Map.of("validation", errors.toString()), locale);
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Map<String, Object>> accessDeniedExceptionHandler(AccessDeniedException e, Locale locale)
	{
		return buildResponseEntity(HttpStatus.FORBIDDEN, "access_denied", null, locale);
	}

	@ExceptionHandler({ InsufficientAuthenticationException.class })
	public ResponseEntity<Map<String, Object>> insufficientAuthenticationExceptionHandler(InsufficientAuthenticationException e, Locale locale)
	{
		return buildResponseEntity(HttpStatus.FORBIDDEN, "access_denied", null, locale);
	}
}
