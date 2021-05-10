package org.urbanclimatemonitor.backend.controller.responses;

import lombok.Builder;
import lombok.Data;
import org.urbanclimatemonitor.backend.entities.Role;

import java.util.List;

@Data
@Builder
public class UserResponse
{
	private String username;

	private Role role;

	private List<Long> locationsWithPermission;
}
