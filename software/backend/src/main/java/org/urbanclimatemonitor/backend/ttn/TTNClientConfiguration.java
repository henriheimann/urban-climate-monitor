package org.urbanclimatemonitor.backend.ttn;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TTNClientConfiguration
{
	@Value("${TTN_APP_ACCESS_KEY}")
	private String appAccessKey;

	@Bean
	public RequestInterceptor basicAuthRequestInterceptor()
	{
		return requestTemplate -> requestTemplate.header("Authorization", "Key " + appAccessKey);
	}
}
