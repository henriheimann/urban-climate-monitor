package org.urbanclimatemonitor.backend.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceDTO
{
	private String id;
	private String locationId;
}
