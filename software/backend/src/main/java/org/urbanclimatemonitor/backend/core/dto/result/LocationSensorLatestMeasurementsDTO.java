package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.urbanclimatemonitor.backend.core.dto.enums.SensorDataType;

import java.util.Map;

@Data
@AllArgsConstructor
public class LocationSensorLatestMeasurementsDTO
{
	private long id;

	private String name;

	private Map<SensorDataType, Object> measurements;
}
