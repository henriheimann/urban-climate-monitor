package org.urbanclimatemonitor.backend.core.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.dto.shared.UploadDTO;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateLocationDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateSensorWithoutLocationDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDataDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationSensorDTO;
import org.urbanclimatemonitor.backend.core.entities.Location;
import org.urbanclimatemonitor.backend.core.entities.Sensor;
import org.urbanclimatemonitor.backend.core.entities.Upload;
import org.urbanclimatemonitor.backend.core.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.util.Streams;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService
{
	private final LocationRepository locationRepository;

	private final SensorRepository sensorRepository;

	public LocationService(LocationRepository locationRepository, SensorRepository sensorRepository)
	{
		this.locationRepository = locationRepository;
		this.sensorRepository = sensorRepository;
	}

	private LocationDTO entityToLocationDto(Location location)
	{
		return LocationDTO.builder()
				.id(location.getId())
				.name(location.getName())
				.icon(new UploadDTO(location.getIcon().getFilename(),
						Base64.getEncoder().encodeToString(location.getIcon().getData())))
				.model3d(new UploadDTO(location.getModel3d().getFilename(),
						Base64.getEncoder().encodeToString(location.getModel3d().getData())))
				.build();
	}

	private LocationSensorDTO entityToLocationSensorDTO(Sensor sensor)
	{
		return new LocationSensorDTO(sensor.getId(), sensor.getName());
	}

	private Upload uploadDtoToUpload(UploadDTO uploadDTO)
	{
		return new Upload(
				uploadDTO.getFilename(),
				Base64.getDecoder().decode(uploadDTO.getData())
		);
	}

	@Transactional
	public List<LocationDTO> getAllLocations()
	{
		return Streams.stream(locationRepository.findAll())
				.map(this::entityToLocationDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public LocationDTO getLocation(long id)
	{
		return locationRepository.findById(id)
				.map(this::entityToLocationDto)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));
	}

	@Transactional
	public LocationDTO createLocation(CreateOrUpdateLocationDTO createLocationDTO)
	{
		Location location = new Location(
				createLocationDTO.getName(),
				uploadDtoToUpload(createLocationDTO.getIcon()),
				uploadDtoToUpload(createLocationDTO.getModel3d())
		);

		locationRepository.save(location);
		return entityToLocationDto(location);
	}

	@Transactional
	public void deleteLocation(long id)
	{
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));
		locationRepository.delete(location);
	}

	@Transactional
	public LocationDTO updateLocation(long id, CreateOrUpdateLocationDTO updateLocationDTO)
	{
		Location location = locationRepository.findById(id)
			.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));

		location.setName(updateLocationDTO.getName());
		location.setIcon(uploadDtoToUpload(updateLocationDTO.getIcon()));
		location.setModel3d(uploadDtoToUpload(updateLocationDTO.getModel3d()));

		locationRepository.save(location);
		return entityToLocationDto(location);
	}

	@Transactional
	public List<LocationSensorDTO> getAllLocationSensors(long id)
	{
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));

		return location.getSensors().stream()
				.map(sensor -> new LocationSensorDTO(sensor.getId(), sensor.getName()))
				.collect(Collectors.toList());
	}

	@Transactional
	public LocationSensorDTO getLocationSensor(long id, long sensorId)
	{
		Sensor sensor = sensorRepository.findById(sensorId)
				.filter(potentialSensor -> potentialSensor.getLocation().getId().equals(id))
				.orElseThrow(() -> new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		return entityToLocationSensorDTO(sensor);
	}

	@Transactional
	public LocationSensorDTO updateLocationSensor(long id, long sensorId, UpdateSensorWithoutLocationDTO updateSensorDTO)
	{
		Sensor sensor = sensorRepository.findById(sensorId)
				.filter(potentialSensor -> potentialSensor.getLocation().getId().equals(id))
				.orElseThrow(() -> new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		sensor.setName(updateSensorDTO.getName());

		sensorRepository.save(sensor);
		return entityToLocationSensorDTO(sensor);
	}

	@Transactional
	public LocationDataDTO getLocationData(long id)
	{
		return null;
	}
}
