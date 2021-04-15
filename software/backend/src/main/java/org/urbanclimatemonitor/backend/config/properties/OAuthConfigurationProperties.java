package org.urbanclimatemonitor.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urbanclimatemonitor.oauth")
public class OAuthConfigurationProperties
{
	@Data
	public static class JwtProperties
	{
		private String clientId;
		private String clientSecret;
		private String signingKey;
		private int tokenValiditySeconds;
		private int refreshTokenValiditySeconds;
	}

	private String resourceId;
	private JwtProperties jwt;
}
