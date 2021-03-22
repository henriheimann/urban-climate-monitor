package org.urbanclimatemonitor.backend.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("org.urbanclimatemonitor.backend.config.properties")
public class ConfigurationPropertiesConfiguration
{
}
