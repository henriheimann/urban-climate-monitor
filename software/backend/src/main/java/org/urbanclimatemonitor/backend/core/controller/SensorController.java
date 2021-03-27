package org.urbanclimatemonitor.backend.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateSensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.SensorDTO;
import org.urbanclimatemonitor.backend.core.services.SensorService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SensorController
{
	private final SensorService sensorService;

	public SensorController(SensorService sensorService)
	{
		this.sensorService = sensorService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/sensors")
	public List<SensorDTO> getAllSensors()
	{
		return sensorService.getAllSensors();
	}

	@PostMapping("/sensors")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorDTO createSensor(@Valid @RequestBody CreateOrUpdateSensorDTO createSensorDTO)
	{
		return sensorService.createSensor(createSensorDTO);
	}

	@GetMapping("/sensors/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorDTO getSensor(@PathVariable long id)
	{
		return sensorService.getSensor(id);
	}

	@DeleteMapping("/sensors/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteSensor(@PathVariable long id)
	{
		sensorService.deleteSensor(id);
	}

	@PutMapping("/sensors/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorDTO updateSensor(@PathVariable long id, @Valid @RequestBody CreateOrUpdateSensorDTO updateSensorDTO)
	{
		return sensorService.updateSensor(id, updateSensorDTO);
	}
}
