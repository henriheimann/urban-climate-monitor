package org.urbanclimatemonitor.backend.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLocationMeasurementsRequest
{
	private ZonedDateTime from;

	private ZonedDateTime to;

	private SensorDataResolution resolution;
}
