package org.urbanclimatemonitor.backend.ttn;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.urbanclimatemonitor.backend.config.properties.TTNConfigurationProperties;

@Component
public class TTNClientConfiguration
{
	private final TTNConfigurationProperties properties;

	public TTNClientConfiguration(TTNConfigurationProperties properties)
	{
		this.properties = properties;
	}

	@Bean
	public RequestInterceptor basicAuthRequestInterceptor()
	{
		return requestTemplate -> requestTemplate.header("Authorization", "Key " + properties.getAppAccessKey());
	}
}
