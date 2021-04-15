package org.urbanclimatemonitor.backend.test.logging;

import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.method.HandlerMethod;


public class CustomMockMvcResultHandler implements ResultHandler
{
	public static ResultHandler log(Logger logger)
	{
		return new CustomMockMvcResultHandler(logger);
	}

	private final Logger logger;

	public CustomMockMvcResultHandler(Logger logger)
	{
		this.logger = logger;
	}

	@Override
	public void handle(MvcResult mvcResult) throws Exception
	{
		MockHttpServletRequest request = mvcResult.getRequest();

		final String prefix;
		if (mvcResult.getHandler() instanceof HandlerMethod) {
			HandlerMethod handlerMethod = ((HandlerMethod)mvcResult.getHandler());
			prefix = "[" + handlerMethod.getMethod().getDeclaringClass().getSimpleName() + "#" + handlerMethod.getMethod().getName() + "] ";
		} else {
			prefix = "";
		}

		String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
		logger.debug("%s<--- %s http://localhost:8080%s%s HTTP/1.1".formatted(prefix, request.getMethod(), request.getRequestURI(), queryString));

		request.getHeaderNames().asIterator().forEachRemaining(
				headerName -> logger.trace("%s%s=%s".formatted(prefix, headerName, request.getHeader(headerName))));

		String requestBody = request.getCharacterEncoding() != null ? request.getContentAsString() : "<no character encoding set>";
		long requestContentLength = request.getContentAsByteArray() != null ? request.getContentAsByteArray().length : 0;

		if (requestContentLength > 0) {
			logger.trace(prefix);
			logger.trace(prefix + requestBody);
		}
		logger.trace("%s<--- END HTTP (%d-byte body)".formatted(prefix, requestContentLength));

		MockHttpServletResponse response = mvcResult.getResponse();

		logger.debug("%s---> HTTP/1.1 %d".formatted(prefix, response.getStatus()));

		response.getHeaderNames().forEach(
				headerName -> logger.trace("%s%s=%s".formatted(prefix, headerName, response.getHeader(headerName))));

		String responseBody = response.getCharacterEncoding() != null ? response.getContentAsString() : "<no character encoding set>";
		long responseContentLength = response.getContentAsByteArray().length;

		if (responseContentLength > 0) {
			logger.trace(prefix);
			logger.trace(prefix + responseBody);
		}
		logger.trace("%s---> END HTTP (%d-byte body)".formatted(prefix, responseContentLength));
	}
}
