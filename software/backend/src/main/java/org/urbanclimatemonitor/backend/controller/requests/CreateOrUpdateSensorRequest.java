package org.urbanclimatemonitor.backend.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateSensorRequest
{
	@NotNull
	@NotBlank
	private String name;

	private Long locationId;
}
