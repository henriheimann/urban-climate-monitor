package org.urbanclimatemonitor.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.urbanclimatemonitor.backend.config.properties.OAuthConfigurationProperties;
import org.urbanclimatemonitor.backend.security.exception.CustomWebResponseExceptionTranslator;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	private final UserDetailsService userService;

	private final CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

	private final OAuthConfigurationProperties properties;

	public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
	                                        PasswordEncoder passwordEncoder, CustomUserDetailsService userService,
	                                        CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator,
	                                        OAuthConfigurationProperties properties)
	{
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.customWebResponseExceptionTranslator = customWebResponseExceptionTranslator;
		this.properties = properties;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient(properties.getJwt().getClientId())
				.secret(passwordEncoder.encode(properties.getJwt().getClientSecret()))
				.accessTokenValiditySeconds(properties.getJwt().getTokenValiditySeconds())
				.refreshTokenValiditySeconds(properties.getJwt().getRefreshTokenValiditySeconds())
				.authorizedGrantTypes("password")
				.scopes("all");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
	{
		endpoints.accessTokenConverter(accessTokenConverter())
				.userDetailsService(userService)
				.authenticationManager(authenticationManager)
				.exceptionTranslator(customWebResponseExceptionTranslator);
	}

	@Bean
	JwtAccessTokenConverter accessTokenConverter()
	{
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(properties.getJwt().getSigningKey());
		return accessTokenConverter;
	}
}
