package org.urbanclimatemonitor.backend.influxdb;

import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.config.properties.InfluxDBConfigurationProperties;

import javax.annotation.PostConstruct;

@Service
@EnableConfigurationProperties(InfluxDBConfigurationProperties.class)
@Log4j2
public class InfluxDBService
{
	private final InfluxDBConfigurationProperties properties;

	private InfluxDB influxDB;

	public InfluxDBService(InfluxDBConfigurationProperties properties)
	{
		this.properties = properties;
	}

	@PostConstruct
	public void postConstruct()
	{
		influxDB = InfluxDBFactory.connect(properties.getUrl(), properties.getUsername(), properties.getPassword());

		Pong response = this.influxDB.ping();
		if (response.getVersion().equalsIgnoreCase("unknown")) {
			throw new RuntimeException("Unable to connect to InfluxDB");
		}

		log.info("Connected to InfluxDB!");
	}

}
