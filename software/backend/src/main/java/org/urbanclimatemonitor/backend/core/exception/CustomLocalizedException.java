package org.urbanclimatemonitor.backend.core.exception;

import lombok.Getter;

import java.util.Objects;

public class CustomLocalizedException extends RuntimeException
{
	@Getter
	private String id;

	@Getter
	private Object[] arguments;

	public CustomLocalizedException(String id)
	{
		this.id = id;
		this.arguments = null;
	}

	public CustomLocalizedException(String id, Objects... arguments)
	{
		this.id = id;
		this.arguments = arguments;
	}

	public CustomLocalizedException(Throwable cause, String id)
	{
		super(cause);
		this.id = id;
		this.arguments = null;
	}

	public CustomLocalizedException(Throwable cause, String id, Object... arguments)
	{
		super(cause);
		this.id = id;
		this.arguments = arguments;
	}
}
