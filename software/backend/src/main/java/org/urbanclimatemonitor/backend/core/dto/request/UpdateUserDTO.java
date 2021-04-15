package org.urbanclimatemonitor.backend.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.urbanclimatemonitor.backend.core.entities.Role;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO
{
	@NotNull
	private Role role;

	private List<Long> locationsWithPermission = new ArrayList<>();

	public UpdateUserDTO(Role role)
	{
		this.role = role;
	}
}
