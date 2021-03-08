package org.urbanclimatemonitor.backend.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.urbanclimatemonitor.backend.core.dto.DeviceDTO;
import org.urbanclimatemonitor.backend.core.services.DeviceService;

import java.util.List;

@RestController
public class DeviceController
{
	private final DeviceService deviceService;

	public DeviceController(DeviceService deviceService)
	{
		this.deviceService = deviceService;
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/devices")
	public List<DeviceDTO> all()
	{
		return deviceService.loadAllDevices();
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}
