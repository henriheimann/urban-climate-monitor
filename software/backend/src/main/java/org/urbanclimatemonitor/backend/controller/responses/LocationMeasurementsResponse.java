package org.urbanclimatemonitor.backend.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class LocationMeasurementsResponse
{
	@Data
	@AllArgsConstructor
	public static class Entry
	{
		private ZonedDateTime timestamp;
		private Map<Long, Object> values;
	}
	
	private List<Entry> entries;
}
