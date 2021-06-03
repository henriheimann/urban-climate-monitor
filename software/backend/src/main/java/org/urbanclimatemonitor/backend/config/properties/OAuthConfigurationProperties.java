package org.urbanclimatemonitor.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urbanclimatemonitor.oauth")
public class OAuthConfigurationProperties
{
	private String resourceId;
	private String clientId;
	private String clientSecret;
	private int tokenValiditySeconds;
	private int refreshTokenValiditySeconds;
}
