package org.urbanclimatemonitor.backend.security.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator
{
	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception
	{
		log.debug(e);
		return super.translate(e);
	}
}
