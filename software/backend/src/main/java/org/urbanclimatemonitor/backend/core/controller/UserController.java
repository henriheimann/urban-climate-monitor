package org.urbanclimatemonitor.backend.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdatePasswordDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.result.UserDTO;
import org.urbanclimatemonitor.backend.core.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "user", description = "User API")
@SecurityRequirement(name = "auth")
@RestController
@PreAuthorize("isAuthenticated()")
public class UserController
{
	private final UserService userService;

	public UserController(UserService userService)
	{
		this.userService = userService;
	}

	@Operation(summary = "Get all users")
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getAllUsers()
	{
		return userService.getAllUsers();
	}

	@Operation(summary = "Create a new user")
	@PostMapping("/user")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO createUser(@Valid @RequestBody CreateUserDTO createUserDTO)
	{
		return userService.createUser(createUserDTO);
	}

	@Operation(summary = "Get a single user")
	@GetMapping("/user/{username}")
	@PreAuthorize("hasRole('ADMIN') || #username == authentication.name")
	public UserDTO getUser(@PathVariable String username)
	{
		return userService.getUser(username);
	}

	@Operation(summary = "Delete a single user")
	@DeleteMapping("/user/{username}")
	@PreAuthorize("hasRole('ADMIN') && #username != authentication.name")
	public void deleteUser(@PathVariable String username)
	{
		userService.deleteUser(username);
	}

	@Operation(summary = "Update a single user")
	@PutMapping("/user/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserDTO updateUserDTO)
	{
		return userService.updateUser(username, updateUserDTO);
	}

	@Operation(summary = "Change the password of a single user")
	@PostMapping("/user/{username}/password")
	@PreAuthorize("#username == authentication.name")
	public void updateUserPassword(@PathVariable String username, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO)
	{
		userService.updateUserPassword(username, updatePasswordDTO);
	}
}
