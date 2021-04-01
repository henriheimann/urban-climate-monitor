package org.urbanclimatemonitor.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.urbanclimatemonitor.backend.config.properties.OAuthConfigurationProperties;
import org.urbanclimatemonitor.backend.security.exception.CustomAuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
	private final OAuthConfigurationProperties properties;

	private final CustomAuthenticationEntryPoint securityExceptionHandler;

	public ResourceServerConfiguration(OAuthConfigurationProperties properties, CustomAuthenticationEntryPoint securityExceptionHandler)
	{
		this.properties = properties;
		this.securityExceptionHandler = securityExceptionHandler;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources)
	{
		resources.resourceId(properties.getResourceId());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers("/oauth/revoke").permitAll().and()
				.authorizeRequests()
				.antMatchers("/**").authenticated().and()
				.exceptionHandling()
				.authenticationEntryPoint(securityExceptionHandler);
	}
}
