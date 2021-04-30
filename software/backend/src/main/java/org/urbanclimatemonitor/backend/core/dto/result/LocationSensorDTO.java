package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.urbanclimatemonitor.backend.core.dto.enums.SensorDataType;

import java.util.Map;

@Data
@AllArgsConstructor
public class LocationSensorDTO
{
	private long id;

	private String name;

	private float[] position;

	private float[] rotation;
}
