package org.urbanclimatemonitor.backend.core.startup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.urbanclimatemonitor.backend.core.dto.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.entities.Role;
import org.urbanclimatemonitor.backend.core.services.UserService;

@Component
public class InitDatabase
{
	private final UserService userService;

	@Value("${org.urbanclimatemonitor.admin.email}")
	private String adminEmail;

	@Value("${org.urabnclimatemonitor.admin.password}")
	private String adminPassword;

	public InitDatabase(UserService userService)
	{
		this.userService = userService;
	}

	@Bean
	CommandLineRunner initUsers() {
		return args -> {
			if (!userService.doesUserWithEmailExist(adminEmail)) {
				userService.createUser(new CreateUserDTO(adminEmail, adminPassword, Role.ADMIN));
			}
		};
	}
}
