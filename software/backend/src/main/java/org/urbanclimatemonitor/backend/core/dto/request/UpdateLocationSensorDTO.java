package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationSensorDTO
{
	@NotNull
	@NotBlank
	private String name;

	@Size(min = 3, max = 3)
	private float[] position;

	@Size(min = 3, max = 3)
	private float[] rotation;
}
