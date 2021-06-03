package org.urbanclimatemonitor.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urbanclimatemonitor.deployment")
public class DeploymentProperties
{
	private String url;
}
