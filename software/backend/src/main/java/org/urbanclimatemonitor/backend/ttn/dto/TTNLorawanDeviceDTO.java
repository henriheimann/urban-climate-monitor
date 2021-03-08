package org.urbanclimatemonitor.backend.ttn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TTNLorawanDeviceDTO
{
	@JsonProperty("app_eui")
	private String applicationEui;

	@JsonProperty("dev_eui")
	private String deviceEui;

	@JsonProperty("app_id")
	private String applicationId;

	@JsonProperty("dev_id")
	private String deviceId;

	@JsonProperty("dev_addr")
	private String deviceAddress;

	@JsonProperty("nwk_s_key")
	private String networkSessionKey;

	@JsonProperty("app_s_key")
	private String applicationSessionKey;

	@JsonProperty("app_key")
	private String applicationKey;

	@JsonProperty("disable_f_cnt_check")
	private Boolean disableFrameCounterChecks;

	@JsonProperty("uses32_bit_f_cnt")
	private Boolean uses32BitFrameCounter;

	@JsonProperty("activation_constraints")
	private String activationConstraints;
}
