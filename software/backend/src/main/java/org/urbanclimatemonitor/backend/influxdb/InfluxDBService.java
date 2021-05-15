package org.urbanclimatemonitor.backend.influxdb;

import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.querybuilder.Select;
import org.influxdb.querybuilder.SelectionQueryImpl;
import org.influxdb.querybuilder.WhereNested;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.config.properties.InfluxDBConfigurationProperties;
import org.urbanclimatemonitor.backend.config.properties.TTNConfigurationProperties;
import org.urbanclimatemonitor.backend.controller.requests.SensorDataResolution;
import org.urbanclimatemonitor.backend.controller.requests.SensorDataType;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.influxdb.querybuilder.BuiltQuery.QueryBuilder.*;
import static org.influxdb.querybuilder.time.DurationLiteral.*;

@Service
@EnableConfigurationProperties(InfluxDBConfigurationProperties.class)
@Log4j2
public class InfluxDBService
{
	private final InfluxDBConfigurationProperties properties;

	private final TTNConfigurationProperties ttnProperties;

	private InfluxDB influxDB;

	public InfluxDBService(InfluxDBConfigurationProperties properties, TTNConfigurationProperties ttnProperties)
	{
		this.properties = properties;
		this.ttnProperties = ttnProperties;
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

	private String getAppId() {
		if (properties.getAppIdForTesting() != null) {
			return properties.getAppIdForTesting();
		} else {
			return ttnProperties.getAppId();
		}
	}

	public Map<SensorDataType, Object> getLatestMeasurements(String ttnDeviceId, Set<SensorDataType> dataTypes)
	{
		return getLatestMeasurements(Set.of(ttnDeviceId), dataTypes).get(ttnDeviceId);
	}

	public Map<String, Map<SensorDataType, Object>> getLatestMeasurements(Set<String> ttnDeviceIds, Set<SensorDataType> dataTypes)
	{
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("SELECT ");
		Set<String> columnNames = dataTypes.stream()
				.map(SensorDataType::getColumnName)
				.collect(Collectors.toSet());
		queryBuilder.append(String.join(", ", columnNames));

		queryBuilder.append(" FROM mqtt_consumer");

		queryBuilder.append(" WHERE ");
		Set<String> whereClauses = ttnDeviceIds.stream()
				.map(ttnDeviceId -> "topic = '" + getAppId() + "/devices/" + ttnDeviceId + "/up'")
				.collect(Collectors.toSet());
		queryBuilder.append(String.join(" OR ", whereClauses));

		queryBuilder.append(" GROUP BY topic ORDER BY DESC LIMIT 1");

		Query query = new Query(queryBuilder.toString(), properties.getDb());
		QueryResult queryResult;

		try {
			queryResult = influxDB.query(query);
		} catch (InfluxDBException exception) {
			throw new CustomLocalizedException(exception, "influxdb-query-error");
		}

		if (queryResult.getResults().size() != 1) {
			throw new CustomLocalizedException("influxdb-query-error");
		}

		QueryResult.Result result = queryResult.getResults().get(0);

		if (result.getSeries() == null) {
			return ttnDeviceIds.stream()
					.collect(Collectors.toMap(ttnDeviceId -> ttnDeviceId, ttnDeviceId -> Map.of()));
		}

		Map<String, Map<SensorDataType, Object>> sensorsMap = new HashMap<>();

		final int prefixLength = (getAppId() + "/devices/").length();
		final int postfixLength = "/up".length();

		for (QueryResult.Series series: result.getSeries()) {
			String topic = series.getTags().get("topic");
			String ttnDeviceId = series.getTags().get("topic").substring(prefixLength, topic.length() - postfixLength);

			Map<SensorDataType, Object> valuesMap = new HashMap<>();

			for (int columnIndex = 0; columnIndex < series.getColumns().size(); columnIndex++) {
				String columnName = series.getColumns().get(columnIndex);
				SensorDataType dataType = SensorDataType.forColumnName(columnName);

				if (dataTypes.contains(dataType)) {
					Object value = series.getValues().get(0).get(columnIndex);
					valuesMap.put(dataType, value);
				}
			}

			sensorsMap.put(ttnDeviceId, valuesMap);
		}

		return sensorsMap;
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

	public Map<String, Map<String, Object>> getMeasurementsForPeriod(Set<String> ttnDeviceIds, SensorDataType dataType,
	                                                                 SensorDataResolution dataResolution,
	                                                                 ZonedDateTime from, ZonedDateTime to)
	{
		SelectionQueryImpl selectionQuery = select();
		selectionQuery.mean(dataType.getColumnName());

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

		Query query = nestedWhere.close().groupBy(groupByColumnFromDataResolution(dataResolution))
				.fill("linear");
		QueryResult queryResult;

		try {
			queryResult = influxDB.query(query);
		} catch (InfluxDBException exception) {
			throw new CustomLocalizedException(exception, "influxdb-query-error");
		}

		return null;
	}
}
