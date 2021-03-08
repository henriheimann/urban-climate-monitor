package org.urbanclimatemonitor.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.urbanclimatemonitor.backend.security.exception.CustomAuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
	@Value("@{oauth.resourceId}")
	private String resourceId;

	private final CustomAuthenticationEntryPoint securityExceptionHandler;

	public ResourceServerConfiguration(CustomAuthenticationEntryPoint securityExceptionHandler)
	{
		this.securityExceptionHandler = securityExceptionHandler;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources)
	{
		resources.resourceId(resourceId);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.antMatcher("/**").authorizeRequests().anyRequest().authenticated().and()
				.exceptionHandling()
				.authenticationEntryPoint(securityExceptionHandler);
	}
}
