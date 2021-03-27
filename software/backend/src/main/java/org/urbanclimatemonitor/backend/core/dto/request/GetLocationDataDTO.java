package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLocationDataDTO
{
	private LocalDateTime from;

	private LocalDateTime to;

	private String type;

	private String resolution;
}
