package org.urbanclimatemonitor.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.entities.User;
import org.urbanclimatemonitor.backend.repositories.UserRepository;
import org.urbanclimatemonitor.backend.exception.CustomLocalizedException;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepository.findById(username).orElseThrow(() -> new CustomLocalizedException("user_not_found"));

		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.roles(user.getRole().name())
				.build();
	}
}
