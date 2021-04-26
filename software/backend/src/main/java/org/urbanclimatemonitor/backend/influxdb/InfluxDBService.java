package org.urbanclimatemonitor.backend.influxdb;

import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.querybuilder.SelectQueryImpl;
import org.influxdb.querybuilder.SelectionQueryImpl;
import org.influxdb.querybuilder.WhereNested;
import org.influxdb.querybuilder.WhereQueryImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.config.properties.InfluxDBConfigurationProperties;
import org.urbanclimatemonitor.backend.core.dto.enums.SensorDataResolution;
import org.urbanclimatemonitor.backend.core.dto.enums.SensorDataType;

import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;
import static org.influxdb.querybuilder.time.DurationLiteral.*;

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

		Pong response = influxDB.ping();
		if (response.getVersion().equalsIgnoreCase("unknown")) {
			throw new RuntimeException("Unable to connect to InfluxDB");
		}

		log.info("Connected to InfluxDB!");
	}

	private List<String> columnsFromDataType(SensorDataType dataType)
	{
		return switch (dataType) {
			case TEMPERATURE -> List.of("payload_fields_temperature", "payload_fields_ir_temperature");
			case HUMIDITY -> List.of("payload_fields_humidity");
			case BRIGHTNESS -> List.of("payload_fields_brightness");
		};
	}

	private Object groupByColumnFromDataResolution(SensorDataResolution dataResolution)
	{
		return switch (dataResolution) {
			case MINUTES -> time(1L, MINUTE);
			case HOURS -> time(1L, HOUR);
			case DAYS -> time(1L, DAY);
			case WEEKS -> time(1L, WEEK);
		};
	}

	private Query buildSensorValuesQuery(List<String> ttnDeviceIds, SensorDataType dataType,
	                                     SensorDataResolution dataResolution, ZonedDateTime from, ZonedDateTime to)
	{
		SelectionQueryImpl selectionQuery = select();

		for (String column: columnsFromDataType(dataType)) {
			selectionQuery = selectionQuery.mean(column);
		}

		@SuppressWarnings("rawtypes")
		WhereNested nestedWhere = selectionQuery
				.from(properties.getDb(), "mqtt_consumer")
				.where(gte("time", from.format(ISO_OFFSET_DATE_TIME)))
				.and(lte("time", to.format(ISO_OFFSET_DATE_TIME)))
				.andNested();

		boolean isFirst = true;
		for (String ttnDeviceId: ttnDeviceIds) {
			String topic = "urban-climate-monitor/devices/%s/up".formatted(ttnDeviceId);
			if (isFirst) {
				isFirst = false;
				nestedWhere = nestedWhere.and(eq("topic", topic));
			} else {
				nestedWhere = nestedWhere.or(eq("topic", topic));
			}
		}

		return nestedWhere
				.close()
				.groupBy(groupByColumnFromDataResolution(dataResolution))
				.fill("linear");
	}



	public void selectSensorValues(String ttnDeviceId)
	{
		Query query = buildSensorValuesQuery(List.of(ttnDeviceId), SensorDataType.HUMIDITY, SensorDataResolution.WEEKS,
				ZonedDateTime.of(LocalDateTime.of(2021, 2, 1, 0, 0, 0), ZoneId.of("Europe/Berlin")),
				ZonedDateTime.of(LocalDateTime.of(2021, 3, 1, 0, 0, 0), ZoneId.of("Europe/Berlin")));

		QueryResult queryResult = influxDB.query(query);
		queryResult.getResults().forEach(result ->
				result.getSeries().forEach(series -> System.out.println(series)));
	}

	public void getMostRecentSensorValues(String ttnDeviceId)
	{

	}
}
