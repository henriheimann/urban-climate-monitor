package org.urbanclimatemonitor.backend.security.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint
{
	private final HandlerExceptionResolver handlerExceptionResolver;

	public CustomAuthenticationEntryPoint(HandlerExceptionResolver handlerExceptionResolver)
	{
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
	{
		handlerExceptionResolver.resolveException(request, response, null, e);
	}
}
