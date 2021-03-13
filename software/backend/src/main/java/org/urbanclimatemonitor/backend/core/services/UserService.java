package org.urbanclimatemonitor.backend.core.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.core.dto.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.entities.User;
import org.urbanclimatemonitor.backend.core.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;

import java.util.stream.Collectors;

@Service
public class UserService
{
	private final UserRepository userRepository;
	private final LocationRepository locationRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, LocationRepository locationRepository,
	                   PasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.locationRepository = locationRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean doesUserWithEmailExist(String email)
	{
		return userRepository.findByEmail(email).isPresent();
	}

	public void createUser(CreateUserDTO createUserDTO)
	{
		User user = new User(
				createUserDTO.getEmail(),
				passwordEncoder.encode(createUserDTO.getPassword()),
				createUserDTO.getRole(),
				createUserDTO.getLocationsWithPermission().stream()
						.map(locationId -> locationRepository.findById(locationId).orElse(null))
						.collect(Collectors.toSet())
		);

		userRepository.save(user);
	}
}
