package org.urbanclimatemonitor.backend.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateLocationDTO;
import org.urbanclimatemonitor.backend.core.dto.request.GetLocationSensorDataDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateLocationSensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDataDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationSensorDTO;
import org.urbanclimatemonitor.backend.core.services.LocationService;
import org.urbanclimatemonitor.backend.core.services.UserService;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "location", description = "Location API")
@RestController
public class LocationController
{
	private final LocationService locationService;

	private final UserService userService;

	public LocationController(LocationService locationService, UserService userService)
	{
		this.locationService = locationService;
		this.userService = userService;
	}

	@Operation(summary = "Get all locations")
	@GetMapping("/locations")
	public List<LocationDTO> getAllLocations()
	{
		return locationService.getAllLocations();
	}

	@Operation(summary = "Create a new location", security = @SecurityRequirement(name = "auth"))
	@PostMapping("/location")
	@PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
	public LocationDTO createLocation(@Valid @RequestBody CreateOrUpdateLocationDTO createLocationDTO)
	{
		return locationService.createLocation(createLocationDTO);
	}

	@Operation(summary = "Get a single location")
	@GetMapping("/location/{id}")
	public LocationDTO getLocation(@PathVariable long id)
	{
		return locationService.getLocation(id);
	}


	@Operation(summary = "Delete a single location", security = @SecurityRequirement(name = "auth"))
	@DeleteMapping("/location/{id}")
	@PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
	public void deleteLocation(@PathVariable long id)
	{
		locationService.deleteLocation(id);
	}

	@Operation(summary = "Update a single location", security = @SecurityRequirement(name = "auth"))
	@PutMapping("/location/{id}")
	@PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
	public LocationDTO updateLocation(@PathVariable long id, @Valid @RequestBody CreateOrUpdateLocationDTO updateLocationDTO)
	{
		return locationService.updateLocation(id, updateLocationDTO);
	}

	@Operation(summary = "Get all sensors of a location")
	@GetMapping("/location/{id}/sensors")
	public List<LocationSensorDTO> getAllLocationSensors(@PathVariable long id)
	{
		return locationService.getAllLocationSensors(id);
	}

	@Operation(summary = "Get a single sensor of a location")
	@GetMapping("/location/{id}/sensors/{sensorId}")
	public LocationSensorDTO getLocationSensor(@PathVariable long id, @PathVariable long sensorId)
	{
		return locationService.getLocationSensor(id, sensorId);
	}

	@Operation(summary = "Update a single sensor of location", security = @SecurityRequirement(name = "auth"))
	@PutMapping("/location/{id}/sensors/{sensorId}")
	@PreAuthorize("isAuthenticated()")
	public LocationSensorDTO updateLocationSensor(@PathVariable long id, @PathVariable long sensorId, @Valid @RequestBody UpdateLocationSensorDTO updateSensorDTO, Principal principal)
	{
		if (!userService.checkUserLocationPermission(principal.getName(), id)) {
			throw new CustomLocalizedException("user-has-no-location-permission", HttpStatus.UNAUTHORIZED);
		}

		return locationService.updateLocationSensor(id, sensorId, updateSensorDTO);
	}

	@Operation(summary = "Get data of all sensors of a single location")
	@GetMapping("/location/{id}/data")
	public LocationDataDTO getLocationData(@PathVariable long id, @Valid @RequestBody GetLocationSensorDataDTO getLocationSensorDataDTO)
	{
		return locationService.getLocationData(id);
	}
}
