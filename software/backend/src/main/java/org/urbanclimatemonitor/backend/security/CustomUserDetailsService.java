package org.urbanclimatemonitor.backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.entities.User;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;
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
