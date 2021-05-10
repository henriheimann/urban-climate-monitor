package org.urbanclimatemonitor.backend.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.urbanclimatemonitor.backend.controller.requests.SensorDataType;

import java.util.Map;

@Data
@AllArgsConstructor
public class SensorResponse
{
	private long id;

	private String name;

	private String ttnId;

	private Long locationId;

	private float[] position;

	private float[] rotation;

	private Map<SensorDataType, Object> measurements;

	public SensorResponse(long id, String name, String ttnId, Long locationId)
	{
		this.id = id;
		this.name = name;
		this.ttnId = ttnId;
		this.locationId = locationId;
	}
}
