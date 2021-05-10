package org.urbanclimatemonitor.backend.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorKeysResponse
{
	private String deviceAddress;

	private String applicationSessionKey;

	private String networkSessionKey;
}
