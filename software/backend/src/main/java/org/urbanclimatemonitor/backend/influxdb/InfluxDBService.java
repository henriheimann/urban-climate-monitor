package org.urbanclimatemonitor.backend.influxdb;

import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBException;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
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
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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

	private String getAppId()
	{
		if (properties.getAppIdForTesting() != null) {
			return properties.getAppIdForTesting();
		} else {
			return ttnProperties.getAppId();
		}
	}

	private String groupByColumnFromDataResolution(SensorDataResolution dataResolution)
	{
		return switch (dataResolution) {
			case MINUTES -> "1m";
			case HOURS -> "1h";
			case DAYS -> "1d";
			case WEEKS -> "1w";
		};
	}

	private StringBuilder createBasicQueryBuilder(String selectString, Set<String> ttnDeviceIds)
	{
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("SELECT ");
		queryBuilder.append(selectString);
		queryBuilder.append(" FROM mqtt_consumer");

		queryBuilder.append(" WHERE (");
		Set<String> whereClauses = ttnDeviceIds.stream()
				.map(ttnDeviceId -> "topic = '" + getAppId() + "/devices/" + ttnDeviceId + "/up'")
				.collect(Collectors.toSet());
		queryBuilder.append(String.join(" OR ", whereClauses));
		queryBuilder.append(")");

		return queryBuilder;
	}

	private QueryResult.Result executeQuery(String queryString)
	{
		Query query = new Query(queryString, properties.getDb());
		QueryResult queryResult;

		try {
			queryResult = influxDB.query(query);
		} catch (InfluxDBException exception) {
			throw new CustomLocalizedException(exception, "influxdb-query-error");
		}

		if (queryResult.getResults().size() != 1) {
			throw new CustomLocalizedException("influxdb-query-error");
		}

		return queryResult.getResults().get(0);
	}

	private Map<String, Map<SensorDataType, Object>> extractSensorMapFromQueryResult(Set<String> ttnDeviceIds,
	                                                                                 Set<SensorDataType> dataTypes,
	                                                                                 QueryResult.Result queryResult)
	{
		if (queryResult.getSeries() == null) {
			return ttnDeviceIds.stream()
					.collect(Collectors.toMap(ttnDeviceId -> ttnDeviceId, ttnDeviceId -> Map.of()));
		}

		Map<String, Map<SensorDataType, Object>> sensorsMap = new HashMap<>();

		final int prefixLength = (getAppId() + "/devices/").length();
		final int postfixLength = "/up".length();

		for (QueryResult.Series series: queryResult.getSeries()) {
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

	public Map<SensorDataType, Object> getLatestMeasurements(String ttnDeviceId, Set<SensorDataType> dataTypes)
	{
		return getLatestMeasurements(Set.of(ttnDeviceId), dataTypes).get(ttnDeviceId);
	}

	public Map<String, Map<SensorDataType, Object>> getLatestMeasurements(Set<String> ttnDeviceIds,
	                                                                      Set<SensorDataType> dataTypes)
	{
		Set<String> columnNames = dataTypes.stream()
				.map(SensorDataType::getColumnName)
				.collect(Collectors.toSet());

		StringBuilder queryBuilder = createBasicQueryBuilder(String.join(", ", columnNames), ttnDeviceIds);
		queryBuilder.append(" GROUP BY topic ORDER BY DESC LIMIT 1");

		QueryResult.Result result = executeQuery(queryBuilder.toString());
		return extractSensorMapFromQueryResult(ttnDeviceIds, dataTypes, result);
	}

	public Map<ZonedDateTime, Map<String, Map<SensorDataType, Object>>> getMeasurementsForPeriod(Set<String> ttnDeviceIds,
	                                                                        Set<SensorDataType> dataTypes,
	                                                                        SensorDataResolution dataResolution,
	                                                                        ZonedDateTime from, ZonedDateTime to)
	{
		Set<String> columnNames = dataTypes.stream()
				.map(dataType -> "mean(" + dataType.getColumnName() + ") as " + dataType.getColumnName())
				.collect(Collectors.toSet());

		StringBuilder queryBuilder = createBasicQueryBuilder(String.join(", ", columnNames), ttnDeviceIds);
		queryBuilder.append(" AND time >= '");
		queryBuilder.append(from.format(ISO_OFFSET_DATE_TIME));
		queryBuilder.append("' AND time <= '");
		queryBuilder.append(to.format(ISO_OFFSET_DATE_TIME));
		queryBuilder.append("'");
		queryBuilder.append(" GROUP BY time(");
		queryBuilder.append(groupByColumnFromDataResolution(dataResolution));
		queryBuilder.append("),topic fill(linear)");

		QueryResult.Result result = executeQuery(queryBuilder.toString());

		if (result.getSeries() == null) {
			return Map.of();
		}

		Map<ZonedDateTime, Map<String, Map<SensorDataType, Object>>> datesMap = new TreeMap<>();

		final int prefixLength = (getAppId() + "/devices/").length();
		final int postfixLength = "/up".length();

		for (QueryResult.Series series: result.getSeries()) {
			String topic = series.getTags().get("topic");
			String ttnDeviceId = series.getTags().get("topic").substring(prefixLength, topic.length() - postfixLength);

			for (int valueIndex = 0; valueIndex < series.getValues().size(); valueIndex++) {

				Map<SensorDataType, Object> valuesMap = new HashMap<>();
				ZonedDateTime time = null;

				for (int columnIndex = 0; columnIndex < series.getColumns().size(); columnIndex++) {
					String columnName = series.getColumns().get(columnIndex);

					if (columnName.equals(SensorDataType.TIME.getColumnName())) {
						time = ZonedDateTime.parse((String)series.getValues().get(valueIndex).get(columnIndex));
					} else {
						SensorDataType dataType = SensorDataType.forColumnName(columnName);
						if (dataTypes.contains(dataType)) {
							Object value = series.getValues().get(valueIndex).get(columnIndex);
							if (value != null) {
								valuesMap.put(dataType, value);
							}
						}
					}
				}

				if (time != null && !valuesMap.isEmpty()) {
					Map<String, Map<SensorDataType, Object>> sensorsMap;
					if (!datesMap.containsKey(time)) {
						sensorsMap = new HashMap<>();
						datesMap.put(time, sensorsMap);
					} else {
						sensorsMap = datesMap.get(time);
					}

					sensorsMap.put(ttnDeviceId, valuesMap);
				}
			}
		}

		return datesMap;
	}
}
