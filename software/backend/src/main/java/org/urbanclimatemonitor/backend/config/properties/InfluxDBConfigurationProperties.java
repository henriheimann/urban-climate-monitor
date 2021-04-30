package org.urbanclimatemonitor.backend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("urbanclimatemonitor.influxdb")
public class InfluxDBConfigurationProperties
{
	private String url;
	private String db;
	private String username;
	private String password;

	private String appIdForTesting;
}
