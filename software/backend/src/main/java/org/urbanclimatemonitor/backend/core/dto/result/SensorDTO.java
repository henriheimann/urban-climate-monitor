package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorDTO
{
	private long id;

	private String name;

	private String ttnId;

	private Long locationId;
}
