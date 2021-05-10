package org.urbanclimatemonitor.backend.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponse
{
	private long id;
	private String filename;
	private String url;
}
