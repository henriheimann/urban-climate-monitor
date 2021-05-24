package org.urbanclimatemonitor.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.controller.requests.CreateOrUpdateSensorRequest;
import org.urbanclimatemonitor.backend.controller.responses.SensorResponse;
import org.urbanclimatemonitor.backend.controller.responses.SensorKeysResponse;
import org.urbanclimatemonitor.backend.services.SensorService;

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
	public List<SensorResponse> getAllSensors()
	{
		return sensorService.getAllSensors();
	}

	@Operation(summary = "Create a new sensor")
	@PostMapping("/sensor")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorResponse createSensor(@Valid @RequestBody CreateOrUpdateSensorRequest createSensorRequest)
	{
		return sensorService.createSensor(createSensorRequest);
	}

	@Operation(summary = "Get a single sensor")
	@GetMapping("/sensor/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorResponse getSensor(@PathVariable long id)
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
	public SensorResponse updateSensor(@PathVariable long id, @Valid @RequestBody CreateOrUpdateSensorRequest updateSensorRequest)
	{
		return sensorService.updateSensor(id, updateSensorRequest);
	}

	@Operation(summary = "Get the TTN keys for a single sensor")
	@GetMapping("/sensor/{id}/keys")
	@PreAuthorize("hasRole('ADMIN')")
	public SensorKeysResponse getSensorKeys(@PathVariable long id)
	{
		return sensorService.getSensorKeys(id);
	}
}
