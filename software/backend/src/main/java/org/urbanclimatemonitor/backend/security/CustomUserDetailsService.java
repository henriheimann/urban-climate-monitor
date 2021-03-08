package org.urbanclimatemonitor.backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.core.exception.CustomLocalizedException;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;

import java.util.List;

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
		return userRepository.findById(username)
				.map(user -> {
					GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
					return new User(user.getEmail(), user.getPassword(), List.of(authority));
				})
				.orElseThrow(() -> new CustomLocalizedException("user_not_found"));
	}
}
