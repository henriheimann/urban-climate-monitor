package org.urbanclimatemonitor.backend.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateLocationRequest
{
	@Data
	public static class Upload
	{
		private long id;
	}

	@Data
	public static class Sensor
	{
		private long id;

		@NotNull
		private String name;

		@NotNull
		@Size(min = 3, max = 3)
		private float[] position;

		@NotNull
		@Size(min = 3, max = 3)
		private float[] rotation;
	}

	@NotNull
	@NotBlank
	private String name;

	@NotNull
	private Upload icon;

	@NotNull
	private Upload model3d;

	private List<Sensor> sensors;
}
