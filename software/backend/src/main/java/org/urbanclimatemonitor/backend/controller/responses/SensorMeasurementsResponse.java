package org.urbanclimatemonitor.backend.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.urbanclimatemonitor.backend.controller.requests.SensorDataType;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class SensorMeasurementsResponse
{
	private ZonedDateTime timestamp;

	private Map<SensorDataType, Object> values;
}
