package org.urbanclimatemonitor.backend.core.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.dto.enums.SensorDataType;
import org.urbanclimatemonitor.backend.core.dto.result.LocationSensorLatestMeasurementsDTO;
import org.urbanclimatemonitor.backend.core.dto.shared.UploadDTO;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateLocationDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateLocationSensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationMeasurementsDTO;
import org.urbanclimatemonitor.backend.core.dto.result.LocationSensorDTO;
import org.urbanclimatemonitor.backend.core.entities.Location;
import org.urbanclimatemonitor.backend.core.entities.Sensor;
import org.urbanclimatemonitor.backend.core.entities.Upload;
import org.urbanclimatemonitor.backend.core.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.influxdb.InfluxDBService;
import org.urbanclimatemonitor.backend.util.Streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocationService
{
	private final LocationRepository locationRepository;

	private final SensorRepository sensorRepository;

	private final InfluxDBService influxDBService;

	public LocationService(LocationRepository locationRepository, SensorRepository sensorRepository,
	                       InfluxDBService influxDBService)
	{
		this.locationRepository = locationRepository;
		this.sensorRepository = sensorRepository;
		this.influxDBService = influxDBService;
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
		float[] position = null;
		float[] rotation = null;

		if (sensor.getLocationPositionX() != null && sensor.getLocationPositionY() != null &&
				sensor.getLocationPositionZ() != null) {
			position = new float[]{
					sensor.getLocationPositionX(),
					sensor.getLocationPositionY(),
					sensor.getLocationPositionZ()
			};
		}

		if (sensor.getLocationRotationX() != null && sensor.getLocationRotationY() != null &&
				sensor.getLocationRotationZ() != null) {
			rotation = new float[]{
					sensor.getLocationPositionX(),
					sensor.getLocationPositionY(),
					sensor.getLocationRotationZ()
			};
		}

		return new LocationSensorDTO(sensor.getId(), sensor.getName(), position, rotation);
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
				.map(this::entityToLocationSensorDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public LocationSensorDTO getLocationSensor(long id, long sensorId)
	{
		Sensor sensor = sensorRepository.findById(sensorId)
				.filter(potentialSensor -> Optional.ofNullable(potentialSensor.getLocation())
						.map(location -> location.getId().equals(id)).orElse(false))
				.orElseThrow(() -> new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		return entityToLocationSensorDTO(sensor);
	}

	@Transactional
	public LocationSensorDTO updateLocationSensor(long id, long sensorId, UpdateLocationSensorDTO updateSensorDTO)
	{
		Sensor sensor = sensorRepository.findById(sensorId)
				.filter(potentialSensor -> Optional.ofNullable(potentialSensor.getLocation())
						.map(location -> location.getId().equals(id)).orElse(false))
				.orElseThrow(() -> new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		sensor.setName(updateSensorDTO.getName());

		if (updateSensorDTO.getPosition() != null) {
			sensor.setLocationPositionX(updateSensorDTO.getPosition()[0]);
			sensor.setLocationPositionY(updateSensorDTO.getPosition()[1]);
			sensor.setLocationPositionZ(updateSensorDTO.getPosition()[2]);
		}

		if (updateSensorDTO.getRotation() != null) {
			sensor.setLocationPositionX(updateSensorDTO.getRotation()[0]);
			sensor.setLocationPositionY(updateSensorDTO.getRotation()[1]);
			sensor.setLocationPositionZ(updateSensorDTO.getRotation()[2]);
		}

		sensorRepository.save(sensor);
		return entityToLocationSensorDTO(sensor);
	}

	@Transactional
	public LocationMeasurementsDTO getLocationMeasurements(long id)
	{
		return null;
	}

	@Transactional
	public List<LocationSensorLatestMeasurementsDTO> getAllLocationSensorsLatestMeasurements(long id)
	{
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));

		Map<String, Sensor> ttnIdsToSensorsMap = location.getSensors().stream()
				.collect(Collectors.toMap(Sensor::getTtnId, sensor -> sensor));

		Map<String, Map<SensorDataType, Object>> ttnIdsToMeasurementsMap =
				influxDBService.getLatestMeasurements(ttnIdsToSensorsMap.keySet(), SensorDataType.setOfAllTypes());

		return ttnIdsToSensorsMap.entrySet().stream()
				.map(entry -> new LocationSensorLatestMeasurementsDTO(
						entry.getValue().getId(),
						entry.getValue().getName(),
						ttnIdsToMeasurementsMap.get(entry.getKey())
				))
				.collect(Collectors.toList());
	}
}
