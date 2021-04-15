package org.urbanclimatemonitor.backend.ttn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceDTO;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceListDTO;

@FeignClient(name="ttn", url= "${urbanclimatemonitor.ttn.url}", configuration = TTNClientConfiguration.class)
public interface TTNClient
{
	@GetMapping(value = "/applications/{applicationId}/devices")
	TTNDeviceListDTO getDevicesForApplication(@PathVariable("applicationId") String applicationId);

	@GetMapping(value = "/applications/{applicationId}/devices/{deviceId}")
	TTNDeviceDTO getDevice(@PathVariable("applicationId") String applicationId,
	                       @PathVariable("deviceId") String deviceId);

	@DeleteMapping(value = "/applications/{applicationId}/devices/{deviceId}")
	TTNDeviceDTO deleteDevice(@PathVariable("applicationId") String applicationId,
	                       @PathVariable("deviceId") String deviceId);

	@PostMapping(value = "/applications/{applicationId}/devices")
	void createDevice(@PathVariable("applicationId") String applicationId, @RequestBody TTNDeviceDTO deviceDTO);
}
