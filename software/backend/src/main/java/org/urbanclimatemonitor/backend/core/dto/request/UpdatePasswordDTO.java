package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDTO
{
	@NotNull
	private String oldPassword;

	@NotNull
	private String newPassword;
}
