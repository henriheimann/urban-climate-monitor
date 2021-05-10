package org.urbanclimatemonitor.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.controller.requests.CreateUserRequest;
import org.urbanclimatemonitor.backend.controller.requests.UpdatePasswordRequest;
import org.urbanclimatemonitor.backend.controller.requests.UpdateUserRequest;
import org.urbanclimatemonitor.backend.controller.responses.UserResponse;
import org.urbanclimatemonitor.backend.entities.Location;
import org.urbanclimatemonitor.backend.entities.Role;
import org.urbanclimatemonitor.backend.entities.User;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.repositories.UserRepository;
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

	private UserResponse entityToUserDto(User user)
	{
		return UserResponse.builder()
				.username(user.getUsername())
				.role(user.getRole())
				.locationsWithPermission(locationsToLocationIds(user.getLocationsWithPermission()))
				.build();
	}

	private User createUserDtoToEntity(CreateUserRequest createUserRequest)
	{
		return new User(
				createUserRequest.getUsername(),
				passwordEncoder.encode(createUserRequest.getPassword()),
				createUserRequest.getRole(),
				locationIdsToLocations(createUserRequest.getLocationsWithPermission())
		);
	}

	@Transactional
	public List<UserResponse> getAllUsers()
	{
		return Streams.stream(userRepository.findAll())
				.map(this::entityToUserDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public UserResponse getUser(String username)
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
	public UserResponse createUser(CreateUserRequest createUserRequest)
	{
		if (userRepository.findById(createUserRequest.getUsername()).isPresent()) {
			throw new CustomLocalizedException("user-already-exists", HttpStatus.CONFLICT);
		}

		User user = createUserDtoToEntity(createUserRequest);
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
	public UserResponse updateUser(String username, UpdateUserRequest updateUserRequest)
	{
		User user = userRepository.findById(username)
				.orElseThrow(() -> new CustomLocalizedException("user-not-found", HttpStatus.NOT_FOUND));
		user.setRole(updateUserRequest.getRole());
		user.setLocationsWithPermission(updateUserRequest.getLocationsWithPermission().stream()
				.map(locationId -> locationRepository.findById(locationId).orElse(null))
				.collect(Collectors.toSet())
		);
		userRepository.save(user);
		return entityToUserDto(user);
	}

	@Transactional
	public void updateUserPassword(String username, UpdatePasswordRequest updatePasswordDTO)
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

		return user.getRole() == Role.ADMIN || user.getLocationsWithPermission()
				.stream().anyMatch(location -> location.getId().equals(locationId));
	}
}
