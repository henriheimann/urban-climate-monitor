package org.urbanclimatemonitor.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.controller.responses.LocationMeasurementsResponse;
import org.urbanclimatemonitor.backend.controller.responses.LocationResponse;
import org.urbanclimatemonitor.backend.controller.requests.CreateOrUpdateLocationRequest;
import org.urbanclimatemonitor.backend.controller.requests.GetLocationMeasurementsRequest;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.services.LocationService;
import org.urbanclimatemonitor.backend.services.UserService;

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
	public List<LocationResponse> getAllLocations()
	{
		return locationService.getAllLocations();
	}

	@Operation(summary = "Create a new location", security = @SecurityRequirement(name = "auth"))
	@PostMapping("/location")
	@PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
	public LocationResponse createLocation(@Valid @RequestBody CreateOrUpdateLocationRequest createLocationRequest)
	{
		return locationService.createLocation(createLocationRequest);
	}

	@Operation(summary = "Get a single location")
	@GetMapping("/location/{id}")
	public LocationResponse getLocation(@PathVariable long id)
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
	@PreAuthorize("isAuthenticated()")
	public LocationResponse updateLocation(@PathVariable long id, @Valid @RequestBody CreateOrUpdateLocationRequest updateLocationRequest, Principal principal)
	{
		if (!userService.checkUserLocationPermission(principal.getName(), id)) {
			throw new CustomLocalizedException("user-has-no-location-permission", HttpStatus.UNAUTHORIZED);
		}

		return locationService.updateLocation(id, updateLocationRequest);
	}

	@Operation(summary = "Get measurements of all sensors of a location")
	@GetMapping("/location/{id}/measurements")
	public LocationMeasurementsResponse getLocationMeasurements(@PathVariable long id, @Valid @RequestBody GetLocationMeasurementsRequest getLocationMeasurementsRequest)
	{
		return locationService.getLocationMeasurements(id);
	}
}
