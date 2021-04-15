package org.urbanclimatemonitor.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Objects;

public class CustomLocalizedException extends RuntimeException
{
	@Getter
	private final String id;

	@Getter
	private final Map<String, String> params;

	@Getter
	private final HttpStatus httpStatus;

	public CustomLocalizedException(String id)
	{
		this.id = id;
		this.params = null;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public CustomLocalizedException(String id, HttpStatus httpStatus)
	{
		this.id = id;
		this.params = null;
		this.httpStatus = httpStatus;
	}

	public CustomLocalizedException(String id, Map<String, String> params)
	{
		this.id = id;
		this.params = params;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public CustomLocalizedException(Throwable cause, String id)
	{
		super(cause);
		this.id = id;
		this.params = null;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public CustomLocalizedException(Throwable cause, String id, Map<String, String> params)
	{
		super(cause);
		this.id = id;
		this.params = params;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
