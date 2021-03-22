package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.urbanclimatemonitor.backend.core.entities.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO
{
	@NotNull
	@NotBlank
	private String username;

	@NotNull
	@NotBlank
	private String password;

	@NotNull
	private Role role;

	private List<Long> locationsWithPermission = new ArrayList<>();

	public CreateUserDTO(String username, String password, Role role)
	{
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
