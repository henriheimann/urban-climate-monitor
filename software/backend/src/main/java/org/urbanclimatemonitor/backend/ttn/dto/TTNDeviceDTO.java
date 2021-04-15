package org.urbanclimatemonitor.backend.ttn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TTNDeviceDTO
{
	@JsonProperty("app_id")
	private String applicationId;

	@JsonProperty("dev_id")
	private String deviceId;

	@JsonProperty("lorawan_device")
	private TTNLorawanDeviceDTO lorawanDevice;
}
