package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorKeysDTO
{
	private String deviceAddress;

	private String applicationSessionKey;

	private String networkSessionKey;
}
