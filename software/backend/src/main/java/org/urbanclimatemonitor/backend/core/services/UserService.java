package org.urbanclimatemonitor.backend.core.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.dto.request.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdatePasswordDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.result.UserDTO;
import org.urbanclimatemonitor.backend.core.entities.Location;
import org.urbanclimatemonitor.backend.core.entities.User;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.core.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;
import org.urbanclimatemonitor.backend.util.Streams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	private Set<Location> locationIdsToLocations(List<Long> locationIds)
	{
		Set<Location> locations = new HashSet<>();
		for (Long locationId : locationIds) {
			locations.add(locationRepository.findById(locationId).orElseThrow());
		}
		return locations;
	}

	private List<Long> locationsToLocationIds(Set<Location> locations)
	{
		return locations.stream().map(Location::getId).collect(Collectors.toList());
	}

	private UserDTO entityToUserDto(User user)
	{
		return UserDTO.builder()
				.username(user.getUsername())
				.role(user.getRole())
				.locationsWithPermission(locationsToLocationIds(user.getLocationsWithPermission()))
				.build();
	}

	private User createUserDtoToEntity(CreateUserDTO createUserDTO)
	{
		return new User(
				createUserDTO.getUsername(),
				passwordEncoder.encode(createUserDTO.getPassword()),
				createUserDTO.getRole(),
				locationIdsToLocations(createUserDTO.getLocationsWithPermission())
		);
	}

	@Transactional
	public List<UserDTO> getAllUsers()
	{
		return Streams.stream(userRepository.findAll())
				.map(this::entityToUserDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public UserDTO getUser(String username)
	{
		return userRepository.findById(username)
				.map(this::entityToUserDto)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));
	}

	public boolean checkUserExists(String username)
	{
		return userRepository.findById(username).isPresent();
	}

	@Transactional
	public UserDTO createUser(CreateUserDTO createUserDTO)
	{
		User user = createUserDtoToEntity(createUserDTO);
		userRepository.save(user);
		return entityToUserDto(user);
	}

	@Transactional
	public void deleteUser(String username)
	{
		User user = userRepository.findById(username)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));
		userRepository.delete(user);
	}

	@Transactional
	public UserDTO updateUser(String username, UpdateUserDTO updateUserDTO)
	{
		User user = userRepository.findById(username)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));
		user.setRole(updateUserDTO.getRole());
		user.setLocationsWithPermission(updateUserDTO.getLocationsWithPermission().stream()
				.map(locationId -> locationRepository.findById(locationId).orElse(null))
				.collect(Collectors.toSet())
		);
		userRepository.save(user);
		return entityToUserDto(user);
	}

	@Transactional
	public void updateUserPassword(String username, UpdatePasswordDTO updatePasswordDTO)
	{
		User user = userRepository.findById(username)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));

		if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
			throw new CustomLocalizedException("wrong-password", HttpStatus.BAD_REQUEST);
		}

		user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
		userRepository.save(user);
	}

	@Transactional
	public void updateUserPasswordInternal(String username, String newPassword)
	{
		User user = userRepository.findById(username)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	@Transactional
	public boolean checkUserLocationPermission(String username, long locationId)
	{
		User user = userRepository.findById(username)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));

		return user.getLocationsWithPermission()
				.stream().anyMatch(location -> location.getId().equals(locationId));
	}
}
