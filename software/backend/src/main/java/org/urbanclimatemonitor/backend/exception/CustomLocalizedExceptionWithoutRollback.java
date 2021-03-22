package org.urbanclimatemonitor.backend.exception;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class CustomLocalizedExceptionWithoutRollback extends CustomLocalizedException
{
	public CustomLocalizedExceptionWithoutRollback(String id)
	{
		super(id);
	}

	public CustomLocalizedExceptionWithoutRollback(String id, HttpStatus httpStatus)
	{
		super(id, httpStatus);
	}

	public CustomLocalizedExceptionWithoutRollback(String id, Objects... arguments)
	{
		super(id, arguments);
	}

	public CustomLocalizedExceptionWithoutRollback(Throwable cause, String id)
	{
		super(cause, id);
	}

	public CustomLocalizedExceptionWithoutRollback(Throwable cause, String id, Object... arguments)
	{
		super(cause, id, arguments);
	}
}
