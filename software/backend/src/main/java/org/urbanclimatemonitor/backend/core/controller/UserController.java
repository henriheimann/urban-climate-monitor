package org.urbanclimatemonitor.backend.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.request.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdatePasswordDTO;
import org.urbanclimatemonitor.backend.core.dto.request.UpdateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.result.UserDTO;
import org.urbanclimatemonitor.backend.core.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController
{
	private final UserService userService;

	public UserController(UserService userService)
	{
		this.userService = userService;
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getAllUsers()
	{
		return userService.getAllUsers();
	}

	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO createUser(@Valid @RequestBody CreateUserDTO createUserDTO)
	{
		return userService.createUser(createUserDTO);
	}

	@GetMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN') || #username == authentication.name")
	public UserDTO getUser(@PathVariable String username)
	{
		return userService.getUser(username);
	}

	@DeleteMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(@PathVariable String username)
	{
		userService.deleteUser(username);
	}

	@PutMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserDTO updateUserDTO)
	{
		return userService.updateUser(username, updateUserDTO);
	}

	@PostMapping("/users/{username}/password")
	@PreAuthorize("#username == authentication.name")
	public void updateUserPassword(@PathVariable String username, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO)
	{
		userService.updateUserPassword(username, updatePasswordDTO);
	}
}
