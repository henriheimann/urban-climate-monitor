package org.urbanclimatemonitor.backend.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.urbanclimatemonitor.backend.config.properties.InitialisationConfigurationProperties;
import org.urbanclimatemonitor.backend.controller.requests.CreateUserRequest;
import org.urbanclimatemonitor.backend.controller.requests.UpdateUserRequest;
import org.urbanclimatemonitor.backend.entities.Role;
import org.urbanclimatemonitor.backend.services.UserService;

@Component
public class InitialiseDatabase implements CommandLineRunner
{
	private final InitialisationConfigurationProperties properties;

	private final UserService userService;

	public InitialiseDatabase(InitialisationConfigurationProperties properties, UserService userService)
	{
		this.properties = properties;
		this.userService = userService;
	}

	@Override
	public void run(String... args)
	{
		if (properties.getAdmin() != null) {
			if (!userService.checkUserExists(properties.getAdmin().getUsername())) {
				userService.createUser(new CreateUserRequest(properties.getAdmin().getUsername(),
						properties.getAdmin().getPassword(), Role.ADMIN));
			} else {
				userService.updateUser(properties.getAdmin().getUsername(), new UpdateUserRequest(Role.ADMIN));
				userService.updateUserPasswordInternal(properties.getAdmin().getUsername(),
						properties.getAdmin().getPassword());
			}
		}
	}
}
