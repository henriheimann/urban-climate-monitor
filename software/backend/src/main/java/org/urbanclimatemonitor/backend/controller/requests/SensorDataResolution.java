package org.urbanclimatemonitor.backend.controller.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SensorDataResolution
{
	@JsonProperty("minutes")
	MINUTES,

	@JsonProperty("hours")
	HOURS,

	@JsonProperty("days")
	DAYS,

	@JsonProperty("weeks")
	WEEKS
}
