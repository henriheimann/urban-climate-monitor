package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.urbanclimatemonitor.backend.core.dto.shared.UploadDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateLocationDTO
{
	@NotNull
	@NotBlank
	private String name;

	@NotNull
	private UploadDTO icon;

	@NotNull
	private UploadDTO model3d;
}
