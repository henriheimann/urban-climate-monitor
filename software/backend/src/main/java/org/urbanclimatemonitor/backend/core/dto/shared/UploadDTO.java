package org.urbanclimatemonitor.backend.core.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO
{
	private String filename;

	private String data;
}
