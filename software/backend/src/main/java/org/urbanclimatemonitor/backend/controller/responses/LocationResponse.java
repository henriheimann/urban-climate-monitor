package org.urbanclimatemonitor.backend.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LocationResponse
{
	private long id;

	private String name;

	private UploadResponse icon;

	private UploadResponse model3d;

	private List<SensorResponse> sensors;

	public LocationResponse(long id, String name, UploadResponse icon, UploadResponse model3d)
	{
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.model3d = model3d;
	}
}
