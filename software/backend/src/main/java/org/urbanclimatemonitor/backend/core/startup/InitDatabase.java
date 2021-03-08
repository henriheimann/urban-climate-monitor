package org.urbanclimatemonitor.backend.core.startup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.urbanclimatemonitor.backend.core.entities.Role;
import org.urbanclimatemonitor.backend.core.entities.User;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;

@Component
public class InitDatabase
{
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	@Value("${UCM_BACKEND_ADMIN_EMAIL}")
	private String adminEmail;

	@Value("${UCM_BACKEND_ADMIN_PASSWORD}")
	private String adminPassword;

	public InitDatabase(PasswordEncoder passwordEncoder, UserRepository userRepository)
	{
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	@Bean
	CommandLineRunner initUsers() {
		return args -> {
			User admin = new User();
			admin.setEmail(adminEmail);
			admin.setPassword(passwordEncoder.encode(adminPassword));
			admin.setRole(Role.ADMIN);
			userRepository.save(admin);
		};
	}
}
