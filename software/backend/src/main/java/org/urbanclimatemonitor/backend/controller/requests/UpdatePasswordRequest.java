package org.urbanclimatemonitor.backend.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest
{
	@NotNull
	private String oldPassword;

	@NotNull
	private String newPassword;
}
