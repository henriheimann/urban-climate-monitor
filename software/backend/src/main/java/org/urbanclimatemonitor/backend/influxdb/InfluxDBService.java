package org.urbanclimatemonitor.backend.influxdb;

import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Log4j2
public class InfluxDBService
{
	@Value("http://${INFLUXDB_HOST}:${INFLUXDB_PORT}")
	private String databaseUrl;

	@Value("${INFLUXDB_USER}")
	private String databaseUser;

	@Value("${INFLUXDB_USER_PASSWORD}")
	private String databaseUserPassword;

	@Value("${INFLUXDB_DB}")
	private String databaseName;

	private InfluxDB influxDB;

	@PostConstruct
	public void postConstruct()
	{
		influxDB = InfluxDBFactory.connect(databaseUrl, databaseUser, databaseUserPassword);

		Pong response = this.influxDB.ping();
		if (response.getVersion().equalsIgnoreCase("unknown")) {
			throw new RuntimeException("Unable to connect to InfluxDB");
		}

		log.info("Connected to InfluxDB!");
	}

}
