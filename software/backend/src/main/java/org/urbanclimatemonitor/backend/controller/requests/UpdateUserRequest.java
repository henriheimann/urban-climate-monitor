package org.urbanclimatemonitor.backend.controller.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.urbanclimatemonitor.backend.entities.Role;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest
{
	@NotNull
	private Role role;

	private List<Long> locationsWithPermission = new ArrayList<>();

	public UpdateUserRequest(Role role)
	{
		this.role = role;
	}
}
