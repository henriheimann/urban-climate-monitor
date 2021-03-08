package org.urbanclimatemonitor.backend.ttn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceListDTO;

@FeignClient(name="ttn", url= "https://${TTN_REGION}.thethings.network:8094", configuration = TTNClientConfiguration.class)
public interface TTNClient
{
	@GetMapping(value = "/applications/{applicationId}/devices")
	TTNDeviceListDTO getDevicesForApplication(@PathVariable("applicationId") String applicationId);
}
