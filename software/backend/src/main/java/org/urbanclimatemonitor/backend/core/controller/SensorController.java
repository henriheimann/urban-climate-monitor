package org.urbanclimatemonitor.backend.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.urbanclimatemonitor.backend.core.dto.SensorDTO;
import org.urbanclimatemonitor.backend.core.services.SensorService;

import java.util.List;

@RestController
public class SensorController
{
	private final SensorService sensorService;

	public SensorController(SensorService sensorService)
	{
		this.sensorService = sensorService;
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/devices")
	public List<SensorDTO> all()
	{
		return sensorService.loadAllDevices();
	}

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}
