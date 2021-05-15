package org.urbanclimatemonitor.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.controller.requests.CreateOrUpdateLocationRequest;
import org.urbanclimatemonitor.backend.controller.requests.GetLocationMeasurementsRequest;
import org.urbanclimatemonitor.backend.controller.requests.SensorDataType;
import org.urbanclimatemonitor.backend.controller.responses.LocationMeasurementsResponse;
import org.urbanclimatemonitor.backend.controller.responses.LocationResponse;
import org.urbanclimatemonitor.backend.controller.responses.SensorResponse;
import org.urbanclimatemonitor.backend.controller.responses.UploadResponse;
import org.urbanclimatemonitor.backend.entities.Location;
import org.urbanclimatemonitor.backend.entities.Sensor;
import org.urbanclimatemonitor.backend.entities.Upload;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.influxdb.InfluxDBService;
import org.urbanclimatemonitor.backend.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.repositories.UploadRepository;
import org.urbanclimatemonitor.backend.util.Streams;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService
{
	private final LocationRepository locationRepository;

	private final UploadRepository uploadRepository;

	private final InfluxDBService influxDBService;

	public LocationService(LocationRepository locationRepository, UploadRepository uploadRepository,
	                       InfluxDBService influxDBService)
	{
		this.locationRepository = locationRepository;
		this.uploadRepository = uploadRepository;
		this.influxDBService = influxDBService;
	}

	private SensorResponse entityToSensorResponse(Sensor sensor,
	                                              Map<String, Map<SensorDataType, Object>> ttnIdsToMeasurementsMap)
	{
		return new SensorResponse(
				sensor.getId(),
				sensor.getName(),
				sensor.getTtnId(),
				sensor.getLocation().getId(),
				new float[]{sensor.getLocationPositionX(), sensor.getLocationPositionY(),
						sensor.getLocationPositionZ()},
				new float[]{sensor.getLocationRotationX(), sensor.getLocationRotationY(),
						sensor.getLocationRotationZ()},
				ttnIdsToMeasurementsMap.get(sensor.getTtnId())
		);
	}

	private LocationResponse entityToLocationResponse(Location location)
	{
		LocationResponse locationResponse = new LocationResponse(
				location.getId(),
				location.getName(),
				new UploadResponse(
						location.getIcon().getId(),
						location.getIcon().getFilename(),
						location.getIcon().getUrl()
				),
				new UploadResponse(
						location.getModel3d().getId(),
						location.getModel3d().getFilename(),
						location.getModel3d().getUrl()
				)
		);

		if (location.getSensors().size() > 0) {
			Map<String, Sensor> ttnIdsToSensorsMap = location.getSensors().stream()
					.collect(Collectors.toMap(Sensor::getTtnId, sensor -> sensor));

			Map<String, Map<SensorDataType, Object>> ttnIdsToMeasurementsMap =
					influxDBService.getLatestMeasurements(ttnIdsToSensorsMap.keySet(), SensorDataType.setOfAllTypes());

			locationResponse.setSensors(
					location.getSensors().stream()
							.map(sensor -> entityToSensorResponse(sensor, ttnIdsToMeasurementsMap))
							.collect(Collectors.toList())
			);
		}

		return locationResponse;
	}

	@Transactional
	public List<LocationResponse> getAllLocations()
	{
		return Streams.stream(locationRepository.findAll())
				.map(this::entityToLocationResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public LocationResponse getLocation(long id)
	{
		return locationRepository.findById(id)
				.map(this::entityToLocationResponse)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));
	}

	@Transactional
	public LocationResponse createLocation(CreateOrUpdateLocationRequest createLocationRequest)
	{
		Upload iconUpload = uploadRepository.findById(createLocationRequest.getIcon().getId())
				.orElseThrow(() -> new CustomLocalizedException("upload-not-found", HttpStatus.NOT_FOUND));

		Upload model3dUpload = uploadRepository.findById(createLocationRequest.getModel3d().getId())
				.orElseThrow(() -> new CustomLocalizedException("upload-not-found", HttpStatus.NOT_FOUND));

		Location location = new Location(
				createLocationRequest.getName(),
				iconUpload,
				model3dUpload
		);

		locationRepository.save(location);
		return entityToLocationResponse(location);
	}

	@Transactional
	public void deleteLocation(long id)
	{
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));
		locationRepository.delete(location);
	}

	@Transactional
	public LocationResponse updateLocation(long id, CreateOrUpdateLocationRequest updateLocationRequest)
	{
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));

		Upload iconUpload = uploadRepository.findById(updateLocationRequest.getIcon().getId())
				.orElseThrow(() -> new CustomLocalizedException("upload-not-found", HttpStatus.NOT_FOUND));

		Upload model3dUpload = uploadRepository.findById(updateLocationRequest.getModel3d().getId())
				.orElseThrow(() -> new CustomLocalizedException("upload-not-found", HttpStatus.NOT_FOUND));

		location.setName(updateLocationRequest.getName());
		location.setIcon(iconUpload);
		location.setModel3d(model3dUpload);

		if (updateLocationRequest.getSensors() != null) {
			Set<Long> currentSensorIds = location.getSensors().stream().map(Sensor::getId).collect(Collectors.toSet());
			Set<Long> requestSensorIds = updateLocationRequest.getSensors().stream()
					.map(CreateOrUpdateLocationRequest.Sensor::getId).collect(Collectors.toSet());

			if (!currentSensorIds.equals(requestSensorIds)) {
				throw new CustomLocalizedException("sensor-ids-mismatch", HttpStatus.BAD_REQUEST);
			}

			updateLocationRequest.getSensors().forEach(requestSensor -> {
				Sensor sensor = location.getSensors().stream().filter(s -> s.getId().equals(requestSensor.getId()))
						.findFirst().orElseThrow();

				sensor.setName(requestSensor.getName());
				sensor.setLocationPositionX(requestSensor.getPosition()[0]);
				sensor.setLocationPositionY(requestSensor.getPosition()[1]);
				sensor.setLocationPositionZ(requestSensor.getPosition()[2]);
				sensor.setLocationRotationX(requestSensor.getRotation()[0]);
				sensor.setLocationRotationY(requestSensor.getRotation()[1]);
				sensor.setLocationRotationZ(requestSensor.getRotation()[2]);
			});
		}

		locationRepository.save(location);
		return entityToLocationResponse(location);
	}

	@Transactional
	public LocationMeasurementsResponse getLocationMeasurements(long id, GetLocationMeasurementsRequest getLocationMeasurementsRequest)
	{
		Location location = locationRepository.findById(id)
				.orElseThrow(() -> new CustomLocalizedException("location-not-found", HttpStatus.NOT_FOUND));

		Set<String> ttnIds = location.getSensors().stream()
				.map(Sensor::getTtnId)
				.collect(Collectors.toSet());

		influxDBService.getMeasurementsForPeriod(ttnIds, getLocationMeasurementsRequest.getType(),
				getLocationMeasurementsRequest.getResolution(), getLocationMeasurementsRequest.getFrom(),
				getLocationMeasurementsRequest.getTo());

		return null;
	}
}
