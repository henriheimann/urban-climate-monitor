package org.urbanclimatemonitor.backend.controller.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum SensorDataType
{
	@JsonProperty("temperature")
	TEMPERATURE("payload_fields_temperature"),

	@JsonProperty("ir_temperature")
	IR_TEMPERATURE("payload_fields_ir_temperature"),

	@JsonProperty("humidity")
	HUMIDITY("payload_fields_humidity"),

	@JsonProperty("brightness_current")
	BRIGHTNESS_CURRENT("payload_fields_brightness_current"),

	@JsonProperty("battery_voltage")
	BATTERY_VOLTAGE("payload_fields_battery_voltage"),

	@JsonProperty("time")
	TIME("time");

	@Getter
	private final String columnName;

	SensorDataType(String columnName)
	{
		this.columnName = columnName;
	}

	public static SensorDataType forColumnName(String columnName)
	{
		return Arrays.stream(SensorDataType.values())
				.filter(sensorDataType -> sensorDataType.getColumnName().equals(columnName))
				.findFirst()
				.orElseThrow();
	}

	public static Set<SensorDataType> setOfAllTypes()
	{
		Set<SensorDataType> set = new HashSet<>();
		Collections.addAll(set, SensorDataType.values());
		return set;
	}
}
