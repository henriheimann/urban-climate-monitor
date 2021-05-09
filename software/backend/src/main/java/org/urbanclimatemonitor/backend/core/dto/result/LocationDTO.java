package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDTO
{
	private long id;

	private String name;

	private String iconUrl;

	private String model3dUrl;
}
