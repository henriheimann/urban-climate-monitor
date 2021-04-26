package org.urbanclimatemonitor.backend.core.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateSensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.SensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.SensorKeysDTO;
import org.urbanclimatemonitor.backend.core.entities.Sensor;
import org.urbanclimatemonitor.backend.core.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedExceptionWithoutRollback;
import org.urbanclimatemonitor.backend.ttn.TTNService;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceDTO;
import org.urbanclimatemonitor.backend.util.Streams;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SensorService
{
	private final SensorRepository sensorRepository;

	private final LocationRepository locationRepository;

	private final TTNService ttnService;

	public SensorService(SensorRepository sensorRepository, LocationRepository locationRepository, TTNService ttnService)
	{
		this.sensorRepository = sensorRepository;
		this.locationRepository = locationRepository;
		this.ttnService = ttnService;
	}

	private SensorDTO entityToSensorDTO(Sensor sensor)
	{
		return new SensorDTO(
				sensor.getId(),
				sensor.getName(),
				sensor.getTtnId(),
				sensor.getLocation() == null ? null : sensor.getLocation().getId()
		);
	}

	@Transactional
	public List<SensorDTO> getAllSensors()
	{
		List<TTNDeviceDTO> ttnDevices = ttnService.getAllDevices();

		// Add new devices from TTN
		ttnDevices.stream()
				.filter(ttnDevice -> sensorRepository.findByTtnId(ttnDevice.getDeviceId()).isEmpty())
				.forEach(ttnDevice -> {
					Sensor sensor = new Sensor();
					sensor.setTtnId(ttnDevice.getDeviceId());
					sensor.setName("Unnamed");
					sensorRepository.save(sensor);
				});

		// Remove sensors with no matching TTN device and return others
		return Streams.stream(sensorRepository.findAll())
				.map(sensor -> {
					if (ttnDevices.stream().noneMatch(ttnDevice -> ttnDevice.getDeviceId().equals(sensor.getTtnId()))) {
						sensorRepository.delete(sensor);
						return null;
					} else {
						return sensor;
					}
				})
				.filter(Objects::nonNull)
				.map(this::entityToSensorDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public SensorDTO createSensor(CreateOrUpdateSensorDTO createSensorDTO)
	{
		Sensor sensor = new Sensor(
				createSensorDTO.getName(),
				createSensorDTO.getLocationId() == null ? null :
						locationRepository.findById(createSensorDTO.getLocationId()).orElseThrow()
		);

		sensorRepository.save(sensor);

		String ttnDeviceId = UUID.randomUUID().toString();
		ttnService.createDevice(ttnDeviceId);

		sensor.setTtnId(ttnDeviceId);
		sensorRepository.save(sensor);

		return entityToSensorDTO(sensor);
	}

	private TTNDeviceDTO makeSureMatchingTTNDeviceExists(Sensor sensor)
	{
		Optional<TTNDeviceDTO> ttnDeviceOptional = ttnService.getDevice(sensor.getTtnId());

		if (ttnDeviceOptional.isEmpty()) {
			sensorRepository.delete(sensor);
			throw new CustomLocalizedExceptionWithoutRollback("ttn-device-does-not-exist", HttpStatus.NOT_FOUND);
		} else {
			return ttnDeviceOptional.get();
		}
	}

	@Transactional(noRollbackFor = {CustomLocalizedExceptionWithoutRollback.class})
	public SensorDTO getSensor(long id)
	{
		Sensor sensor = sensorRepository.findById(id).orElseThrow(() ->
				new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		makeSureMatchingTTNDeviceExists(sensor);

		return entityToSensorDTO(sensor);
	}

	@Transactional(noRollbackFor = {CustomLocalizedExceptionWithoutRollback.class})
	public void deleteSensor(long id)
	{
		Sensor sensor = sensorRepository.findById(id).orElseThrow(() ->
				new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		makeSureMatchingTTNDeviceExists(sensor);

		ttnService.deleteDevice(sensor.getTtnId());
		sensorRepository.delete(sensor);
	}

	@Transactional(noRollbackFor = {CustomLocalizedExceptionWithoutRollback.class})
	public SensorDTO updateSensor(long id, CreateOrUpdateSensorDTO updateSensorDTO)
	{
		Sensor sensor = sensorRepository.findById(id).orElseThrow(() ->
				new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		makeSureMatchingTTNDeviceExists(sensor);

		sensor.setName(updateSensorDTO.getName());
		sensor.setLocation(updateSensorDTO.getLocationId() == null ? null :
				locationRepository.findById(updateSensorDTO.getLocationId()).orElseThrow());

		return entityToSensorDTO(sensor);
	}

	@Transactional(noRollbackFor = {CustomLocalizedExceptionWithoutRollback.class})
	public SensorKeysDTO getSensorKeys(long id)
	{
		Sensor sensor = sensorRepository.findById(id).orElseThrow(() ->
				new CustomLocalizedException("sensor-not-found", HttpStatus.NOT_FOUND));

		TTNDeviceDTO ttnDeviceDTO = makeSureMatchingTTNDeviceExists(sensor);

		return new SensorKeysDTO(
				ttnDeviceDTO.getLorawanDevice().getDeviceAddress(),
				ttnDeviceDTO.getLorawanDevice().getApplicationSessionKey(),
				ttnDeviceDTO.getLorawanDevice().getNetworkSessionKey()
		);
	}
}
