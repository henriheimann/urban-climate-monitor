package org.urbanclimatemonitor.backend.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateSensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.SensorDTO;
import org.urbanclimatemonitor.backend.core.services.SensorService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "sensor", description = "Sensor API")
@SecurityRequirement(name = "auth")
@RestController
@PreAuthorize("isAuthenticated()")
public class SensorController
{
	private final SensorService sensorService;

	public SensorController(SensorService sensorService)
	{
		this.sensorService = sensorService;
	}

	@Operation(summary = "Get all sensors")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/sensors")
	public List<SensorDTO> getAllSensors()
	{
		return sensorService.getAllSensors();
	}

	@Operation(summary = "Create a new sensor")
	@PostMapping("/sensor")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorDTO createSensor(@Valid @RequestBody CreateOrUpdateSensorDTO createSensorDTO)
	{
		return sensorService.createSensor(createSensorDTO);
	}

	@Operation(summary = "Get a single sensor")
	@GetMapping("/sensor/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorDTO getSensor(@PathVariable long id)
	{
		return sensorService.getSensor(id);
	}

	@Operation(summary = "Delete a single sensor")
	@DeleteMapping("/sensor/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteSensor(@PathVariable long id)
	{
		sensorService.deleteSensor(id);
	}

	@Operation(summary = "Update a single sensor")
	@PutMapping("/sensor/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorDTO updateSensor(@PathVariable long id, @Valid @RequestBody CreateOrUpdateSensorDTO updateSensorDTO)
	{
		return sensorService.updateSensor(id, updateSensorDTO);
	}
}
