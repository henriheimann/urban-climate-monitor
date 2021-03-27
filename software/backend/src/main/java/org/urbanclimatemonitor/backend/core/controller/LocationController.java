package org.urbanclimatemonitor.backend.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateLocationDTO;
import org.urbanclimatemonitor.backend.core.dto.request.GetLocationDataDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateSensorWithoutLocationDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDataDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationSensorDTO;
import org.urbanclimatemonitor.backend.core.services.LocationService;
import org.urbanclimatemonitor.backend.core.services.UserService;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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

	@GetMapping("/locations")
	public List<LocationDTO> getAllLocations()
	{
		return locationService.getAllLocations();
	}

	@PostMapping("/locations")
	@PreAuthorize("hasRole('ADMIN')")
	public LocationDTO createLocation(@Valid @RequestBody CreateOrUpdateLocationDTO createLocationDTO)
	{
		return locationService.createLocation(createLocationDTO);
	}

	@GetMapping("/locations/{id}")
	public LocationDTO getLocation(@PathVariable long id)
	{
		return locationService.getLocation(id);
	}

	@DeleteMapping("/locations/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteLocation(@PathVariable long id)
	{
		locationService.deleteLocation(id);
	}

	@PutMapping("/locations/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public LocationDTO updateLocation(@PathVariable long id, @Valid @RequestBody CreateOrUpdateLocationDTO updateLocationDTO)
	{
		return locationService.updateLocation(id, updateLocationDTO);
	}

	@GetMapping("/locations/{id}/sensors")
	public List<LocationSensorDTO> getAllLocationSensors(@PathVariable long id)
	{
		return locationService.getAllLocationSensors(id);
	}

	@GetMapping("/locations/{id}/sensors/{sensorId}")
	public LocationSensorDTO getLocationSensor(@PathVariable long id, @PathVariable long sensorId)
	{
		return locationService.getLocationSensor(id, sensorId);
	}

	@PutMapping("/locations/{id}/sensors/{sensorId}")
	public LocationSensorDTO updateLocationSensor(@PathVariable long id, @PathVariable long sensorId, @Valid @RequestBody UpdateSensorWithoutLocationDTO updateSensorDTO, Principal principal)
	{
		if (!userService.checkUserLocationPermission(principal.getName(), id)) {
			throw new CustomLocalizedException("user-has-no-location-permission", HttpStatus.UNAUTHORIZED);
		}

		return locationService.updateLocationSensor(id, sensorId, updateSensorDTO);
	}

	@GetMapping("/locations/{id}/data")
	public LocationDataDTO getLocationData(@PathVariable long id, @Valid @RequestBody GetLocationDataDTO getLocationDataDTO)
	{
		return locationService.getLocationData(id);
	}
}
