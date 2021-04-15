package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLocationSensorDataDTO
{
	private ZonedDateTime from;

	private ZonedDateTime to;

	private String type;

	private String resolution;
}
