package org.urbanclimatemonitor.backend.influxdb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.urbanclimatemonitor.backend.config.properties.TTNConfigurationProperties;
import org.urbanclimatemonitor.backend.controller.requests.SensorDataType;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = { InfluxDBService.class, TTNConfigurationProperties.class })
class InfluxDBServiceTest
{
	@Autowired
	private InfluxDBService influxDBService;

	@Test
	public void getLatestMeasurements_returnsAllMeasurements_ifRequested()
	{
		Map<SensorDataType, Object> latestMeasurements =
				influxDBService.getLatestMeasurements("002-pcb-prototype", SensorDataType.setOfAllTypes());

		assertThat(latestMeasurements).containsEntry(SensorDataType.TEMPERATURE, 23.04);
		assertThat(latestMeasurements).containsEntry(SensorDataType.IR_TEMPERATURE, 34.5);
		assertThat(latestMeasurements).containsEntry(SensorDataType.HUMIDITY, 54.73);
		assertThat(latestMeasurements).containsEntry(SensorDataType.BRIGHTNESS_CURRENT, 2333.0);
		assertThat(latestMeasurements).containsEntry(SensorDataType.TIME, "2021-03-27T16:05:33.264426505Z");
	}

	@Test
	public void getLatestMeasurements_onlyReturnsTemperature_ifRequested()
	{
		Map<SensorDataType, Object> latestMeasurements =
				influxDBService.getLatestMeasurements("002-pcb-prototype", Set.of(SensorDataType.TEMPERATURE));

		assertThat(latestMeasurements).containsEntry(SensorDataType.TEMPERATURE, 23.04);
		assertThat(latestMeasurements).hasSize(1);
	}
}