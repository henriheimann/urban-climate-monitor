package org.urbanclimatemonitor.backend.core.dto.result;

import lombok.Builder;
import lombok.Data;
import org.urbanclimatemonitor.backend.core.entities.Role;

import java.util.List;

@Data
@Builder
public class UserDTO
{
	private String username;

	private Role role;

	private List<Long> locationsWithPermission;
}
