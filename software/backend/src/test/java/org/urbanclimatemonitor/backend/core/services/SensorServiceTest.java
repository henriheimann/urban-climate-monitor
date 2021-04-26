package org.urbanclimatemonitor.backend.core.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.urbanclimatemonitor.backend.core.dto.request.CreateOrUpdateSensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.SensorDTO;
import org.urbanclimatemonitor.backend.core.dto.result.SensorKeysDTO;
import org.urbanclimatemonitor.backend.core.entities.Location;
import org.urbanclimatemonitor.backend.core.entities.Sensor;
import org.urbanclimatemonitor.backend.core.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedExceptionWithoutRollback;
import org.urbanclimatemonitor.backend.test.TestEntities;
import org.urbanclimatemonitor.backend.test.mockito.SaveSensorAnswer;
import org.urbanclimatemonitor.backend.ttn.TTNService;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceDTO;
import org.urbanclimatemonitor.backend.ttn.dto.TTNLorawanDeviceDTO;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest
{
	@Mock
	private SensorRepository sensorRepository;

	@Mock
	private LocationRepository locationRepository;

	@Mock
	private TTNService ttnService;

	private SensorService sensorService;

	@BeforeEach
	public void setUp()
	{
		sensorService = new SensorService(sensorRepository, locationRepository, ttnService);
	}

	@Test
	public void loadAllSensors_addsSensor_ifDeviceExistsOnlyInTTN()
	{
		TTNDeviceDTO ttnDevice1 = TestEntities.ttnDeviceDTO("5ee00e6d-bd66-463f-a472-005534fe46d6");
		TTNDeviceDTO ttnDevice2 = TestEntities.ttnDeviceDTO("38d5f56e-42d6-413e-b1c4-607bdc05ed20");
		List<TTNDeviceDTO> ttnDeviceDTOList = List.of(ttnDevice1, ttnDevice2);
		when(ttnService.getAllDevices()).thenReturn(ttnDeviceDTOList);

		Sensor sensor1 = TestEntities.sensor(14L, "5ee00e6d-bd66-463f-a472-005534fe46d6");
		Sensor sensor2 = TestEntities.sensor(29L, "38d5f56e-42d6-413e-b1c4-607bdc05ed20");
		List<Sensor> sensorList = List.of(sensor1, sensor2);
		when(sensorRepository.findByTtnId("5ee00e6d-bd66-463f-a472-005534fe46d6")).thenReturn(Optional.of(sensor1));
		when(sensorRepository.findByTtnId("38d5f56e-42d6-413e-b1c4-607bdc05ed20")).thenReturn(Optional.empty());
		when(sensorRepository.findAll()).thenReturn(sensorList);

		when(sensorRepository.save(argThat(sensor -> sensor.getTtnId().equals("38d5f56e-42d6-413e-b1c4-607bdc05ed20"))))
				.thenAnswer(new SaveSensorAnswer(29L));

		List<SensorDTO> sensorDTOList = sensorService.getAllSensors();

		verify(sensorRepository).save(argThat(sensor -> sensor.getTtnId().equals("38d5f56e-42d6-413e-b1c4-607bdc05ed20")));
		assertThat(sensorDTOList).hasSize(2);
		assertThat(sensorDTOList).anyMatch((sensorDTO -> sensorDTO.getId() == 14L));
		assertThat(sensorDTOList).anyMatch((sensorDTO -> sensorDTO.getId() == 29L));
	}

	@Test
	public void loadAllSensors_deletesSensor_ifDeviceIsNotReturnedByTTN()
	{
		TTNDeviceDTO ttnDevice1 = TestEntities.ttnDeviceDTO("5ee00e6d-bd66-463f-a472-005534fe46d6");
		TTNDeviceDTO ttnDevice2 = TestEntities.ttnDeviceDTO("38d5f56e-42d6-413e-b1c4-607bdc05ed20");
		List<TTNDeviceDTO> ttnDeviceDTOList = List.of(ttnDevice1, ttnDevice2);
		when(ttnService.getAllDevices()).thenReturn(ttnDeviceDTOList);

		Sensor sensor1 = TestEntities.sensor(14L, "5ee00e6d-bd66-463f-a472-005534fe46d6");
		Sensor sensor2 = TestEntities.sensor(29L, "38d5f56e-42d6-413e-b1c4-607bdc05ed20");
		Sensor sensor3 = TestEntities.sensor(32L, "5dd5ecb4-21ae-4e52-8d67-bdaa5ef2957b");
		List<Sensor> sensorList = List.of(sensor1, sensor2, sensor3);
		when(sensorRepository.findByTtnId("5ee00e6d-bd66-463f-a472-005534fe46d6")).thenReturn(Optional.of(sensor1));
		when(sensorRepository.findByTtnId("38d5f56e-42d6-413e-b1c4-607bdc05ed20")).thenReturn(Optional.of(sensor2));
		when(sensorRepository.findAll()).thenReturn(sensorList);

		List<SensorDTO> sensorDTOList = sensorService.getAllSensors();

		verify(sensorRepository).delete(argThat(sensor -> sensor.getId() == 32L));
		assertThat(sensorDTOList).hasSize(2);
		assertThat(sensorDTOList).anyMatch((sensorDTO -> sensorDTO.getId() == 14L));
		assertThat(sensorDTOList).anyMatch((sensorDTO -> sensorDTO.getId() == 29L));
	}

	@Test
	public void createSensor()
	{
		Location location = TestEntities.location(98L, "Location Name");
		when(locationRepository.findById(98L)).thenReturn(Optional.of(location));

		when(sensorRepository.save(any())).thenAnswer(new SaveSensorAnswer(4124L));

		CreateOrUpdateSensorDTO createSensorDTO = new CreateOrUpdateSensorDTO("Name", 98L);
		SensorDTO sensorDTO = sensorService.createSensor(createSensorDTO);

		verify(ttnService).createDevice(any());
		assertThat(sensorDTO.getId()).isEqualTo(4124L);
		assertThat(sensorDTO.getTtnId()).isNotNull();
		assertThat(sensorDTO.getName()).isEqualTo("Name");
		assertThat(sensorDTO.getLocationId()).isEqualTo(98L);
	}

	@Test
	public void getSensor()
	{
		TTNDeviceDTO ttnDevice = TestEntities.ttnDeviceDTO("5ee00e6d-bd66-463f-a472-005534fe46d6");
		when(ttnService.getDevice("5ee00e6d-bd66-463f-a472-005534fe46d6")).thenReturn(Optional.of(ttnDevice));

		Sensor sensor = TestEntities.sensor(14L, "5ee00e6d-bd66-463f-a472-005534fe46d6", "Sensor Name");
		when(sensorRepository.findById(14L)).thenReturn(Optional.of(sensor));

		SensorDTO sensorDTO = sensorService.getSensor(14L);
		assertThat(sensorDTO.getId()).isEqualTo(14L);
		assertThat(sensorDTO.getTtnId()).isEqualTo("5ee00e6d-bd66-463f-a472-005534fe46d6");
		assertThat(sensorDTO.getName()).isEqualTo("Sensor Name");
		assertThat(sensorDTO.getLocationId()).isNull();
	}

	@Test
	public void getSensor_throwsAnException_ifSensorDoesNotExist()
	{
		when(sensorRepository.findById(14L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> sensorService.getSensor(14L))
				.isInstanceOf(CustomLocalizedException.class)
				.extracting("id", "httpStatus").contains("sensor-not-found", HttpStatus.NOT_FOUND);
	}

	@Test
	public void getSensor_throwsAnExceptionAndDeletesSensor_ifDeviceIsNotReturnedByTTN()
	{
		Sensor sensor = TestEntities.sensor(14L, "00000014", "Sensor Name");
		when(sensorRepository.findById(14L)).thenReturn(Optional.of(sensor));

		assertThatThrownBy(() -> sensorService.getSensor(14L))
				.isInstanceOf(CustomLocalizedExceptionWithoutRollback.class)
				.extracting("id", "httpStatus").contains("ttn-device-does-not-exist", HttpStatus.NOT_FOUND);

		verify(sensorRepository).delete(argThat(s -> s.getId() == 14L));
	}

	@Test
	public void deleteSensor()
	{
		TTNDeviceDTO ttnDevice = TestEntities.ttnDeviceDTO("5ee00e6d-bd66-463f-a472-005534fe46d6");
		when(ttnService.getDevice("5ee00e6d-bd66-463f-a472-005534fe46d6")).thenReturn(Optional.of(ttnDevice));

		Sensor sensor = TestEntities.sensor(14L, "5ee00e6d-bd66-463f-a472-005534fe46d6", "Sensor Name");
		when(sensorRepository.findById(14L)).thenReturn(Optional.of(sensor));

		sensorService.deleteSensor(14L);

		verify(ttnService).deleteDevice("5ee00e6d-bd66-463f-a472-005534fe46d6");
		verify(sensorRepository).delete(argThat(s -> s.getId() == 14L));
	}

	@Test
	public void updateSensor()
	{
		TTNDeviceDTO ttnDevice = TestEntities.ttnDeviceDTO("5ee00e6d-bd66-463f-a472-005534fe46d6");
		when(ttnService.getDevice("5ee00e6d-bd66-463f-a472-005534fe46d6")).thenReturn(Optional.of(ttnDevice));

		Sensor sensor = TestEntities.sensor(14L, "5ee00e6d-bd66-463f-a472-005534fe46d6", "Sensor Name");
		when(sensorRepository.findById(14L)).thenReturn(Optional.of(sensor));

		Location location = TestEntities.location(98L, "Location Name");
		when(locationRepository.findById(98L)).thenReturn(Optional.of(location));

		CreateOrUpdateSensorDTO updateSensorDTO = new CreateOrUpdateSensorDTO("New Name", 98L);
		SensorDTO sensorDTO = sensorService.updateSensor(14L, updateSensorDTO);

		assertThat(sensorDTO.getId()).isEqualTo(14L);
		assertThat(sensorDTO.getTtnId()).isEqualTo("5ee00e6d-bd66-463f-a472-005534fe46d6");
		assertThat(sensorDTO.getName()).isEqualTo("New Name");
		assertThat(sensorDTO.getLocationId()).isEqualTo(98L);
	}

	@Test
	public void getSensorKeys()
	{
		TTNLorawanDeviceDTO lorawanDeviceDTO = new TTNLorawanDeviceDTO();
		lorawanDeviceDTO.setDeviceAddress("1234");
		lorawanDeviceDTO.setApplicationSessionKey("AABB");
		lorawanDeviceDTO.setNetworkSessionKey("1122");

		TTNDeviceDTO ttnDeviceDTO = new TTNDeviceDTO();
		ttnDeviceDTO.setDeviceId("5ee00e6d-bd66-463f-a472-005534fe46d6");
		ttnDeviceDTO.setLorawanDevice(lorawanDeviceDTO);

		when(ttnService.getDevice("5ee00e6d-bd66-463f-a472-005534fe46d6")).thenReturn(Optional.of(ttnDeviceDTO));

		Sensor sensor = TestEntities.sensor(14L, "5ee00e6d-bd66-463f-a472-005534fe46d6", "Sensor Name");
		when(sensorRepository.findById(14L)).thenReturn(Optional.of(sensor));

		SensorKeysDTO sensorKeysDTO = sensorService.getSensorKeys(14L);
		assertThat(sensorKeysDTO.getDeviceAddress()).isEqualTo("1234");
		assertThat(sensorKeysDTO.getApplicationSessionKey()).isEqualTo("AABB");
		assertThat(sensorKeysDTO.getNetworkSessionKey()).isEqualTo("1122");
	}
}