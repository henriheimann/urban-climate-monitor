package org.urbanclimatemonitor.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urbanclimatemonitor.ttn")
public class TTNConfigurationProperties
{
	private String url;
	private String appId;
	private String appAccessKey;
	private String appEui;
	private String deviceIdPrefix;
}
