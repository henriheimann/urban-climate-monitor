package org.urbanclimatemonitor.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class CustomLocalizedException extends RuntimeException
{
	@Getter
	private final String id;

	@Getter
	private final Object[] arguments;

	@Getter
	private final HttpStatus httpStatus;

	public CustomLocalizedException(String id)
	{
		this.id = id;
		this.arguments = null;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public CustomLocalizedException(String id, HttpStatus httpStatus)
	{
		this.id = id;
		this.arguments = null;
		this.httpStatus = httpStatus;
	}

	public CustomLocalizedException(String id, Objects... arguments)
	{
		this.id = id;
		this.arguments = arguments;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public CustomLocalizedException(Throwable cause, String id)
	{
		super(cause);
		this.id = id;
		this.arguments = null;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public CustomLocalizedException(Throwable cause, String id, Object... arguments)
	{
		super(cause);
		this.id = id;
		this.arguments = arguments;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
