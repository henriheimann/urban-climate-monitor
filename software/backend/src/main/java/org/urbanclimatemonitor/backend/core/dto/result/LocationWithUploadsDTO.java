package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.Builder;
import lombok.Data;
import org.urbanclimatemonitor.backend.core.dto.shared.UploadDTO;

@Data
@Builder
public class LocationWithUploadsDTO
{
	private long id;

	private String name;

	private UploadDTO icon;

	private UploadDTO model3d;
}
