package org.urbanclimatemonitor.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urbanclimatemonitor.init")
public class InitialisationConfigurationProperties
{
	@Data
	public static class Credentials
	{
		private String username;
		private String password;
	}

	private Credentials admin;
}
