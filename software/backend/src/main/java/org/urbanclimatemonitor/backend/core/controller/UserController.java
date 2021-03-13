package org.urbanclimatemonitor.backend.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.urbanclimatemonitor.backend.core.dto.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.dto.UserDTO;
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
		return null;
	}

	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public void createUser(@Valid @RequestBody CreateUserDTO createUserDTO)
	{
		userService.createUser(createUserDTO);
	}

	@GetMapping("/users/{userEmail}")
	@PreAuthorize("hasRole('ADMIN') || #userEmail == authentication.principal.username")
	public UserDTO getUser(@PathVariable String userEmail)
	{
		return null;
	}

	@DeleteMapping("/users/{userEmail}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(@PathVariable String userEmail)
	{

	}

	@PutMapping("/users/{userEmail}")
	@PreAuthorize("hasRole('ADMIN')")
	public void updateUser(@PathVariable String userEmail)
	{

	}

	@PutMapping("/users/{userEmail}/password")
	@PreAuthorize("#userEmail == authentication.principal.username")
	public void updateUserPassword(@PathVariable String userEmail)
	{

	}
}
