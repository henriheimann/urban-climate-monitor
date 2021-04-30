package org.urbanclimatemonitor.backend.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.urbanclimatemonitor.backend.config.properties.InitialisationConfigurationProperties;
import org.urbanclimatemonitor.backend.core.dto.enums.SensorDataType;
import org.urbanclimatemonitor.backend.core.dto.request.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateUserDTO;
import org.urbanclimatemonitor.backend.core.entities.Role;
import org.urbanclimatemonitor.backend.core.services.UserService;
import org.urbanclimatemonitor.backend.influxdb.InfluxDBService;
import org.urbanclimatemonitor.backend.ttn.TTNService;

import java.util.Set;

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
				userService.createUser(new CreateUserDTO(properties.getAdmin().getUsername(),
						properties.getAdmin().getPassword(), Role.ADMIN));
			} else {
				userService.updateUser(properties.getAdmin().getUsername(), new UpdateUserDTO(Role.ADMIN));
				userService.updateUserPasswordInternal(properties.getAdmin().getUsername(),
						properties.getAdmin().getPassword());
			}
		}
	}
}
