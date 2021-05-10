package org.urbanclimatemonitor.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.controller.requests.CreateUserRequest;
import org.urbanclimatemonitor.backend.controller.requests.UpdatePasswordRequest;
import org.urbanclimatemonitor.backend.controller.requests.UpdateUserRequest;
import org.urbanclimatemonitor.backend.controller.responses.UserResponse;
import org.urbanclimatemonitor.backend.services.UserService;

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
	public List<UserResponse> getAllUsers()
	{
		return userService.getAllUsers();
	}

	@Operation(summary = "Create a new user")
	@PostMapping("/user")
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponse createUser(@Valid @RequestBody CreateUserRequest createUserRequest)
	{
		return userService.createUser(createUserRequest);
	}

	@Operation(summary = "Get a single user")
	@GetMapping("/user/{username}")
	@PreAuthorize("hasRole('ADMIN') || #username == authentication.name")
	public UserResponse getUser(@PathVariable String username)
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
	public UserResponse updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserRequest updateUserRequest)
	{
		return userService.updateUser(username, updateUserRequest);
	}

	@Operation(summary = "Change the password of a single user")
	@PostMapping("/user/{username}/password")
	@PreAuthorize("#username == authentication.name")
	public void updateUserPassword(@PathVariable String username, @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest)
	{
		userService.updateUserPassword(username, updatePasswordRequest);
	}
}
