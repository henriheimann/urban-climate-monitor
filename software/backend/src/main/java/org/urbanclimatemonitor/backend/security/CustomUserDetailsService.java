package org.urbanclimatemonitor.backend.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.core.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		return userRepository.findByEmail(username)
				.map(user -> User.builder()
						.username(user.getEmail())
						.password(user.getPassword())
						.roles(user.getRole().name())
						.build()
				)
				.orElseThrow(() -> new CustomLocalizedException("user_not_found"));
	}
}
