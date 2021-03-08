package org.urbanclimatemonitor.backend.security;

import org.springframework.beans.factory.annotation.Value;
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
import org.urbanclimatemonitor.backend.security.exception.CustomWebResponseExceptionTranslator;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	private final UserDetailsService userService;

	private final CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

	public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
	                                        PasswordEncoder passwordEncoder, CustomUserDetailsService userService,
	                                        CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator)
	{
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.customWebResponseExceptionTranslator = customWebResponseExceptionTranslator;
	}

	@Value("${oauth.jwt.clientId}")
	private String clientId;

	@Value("${oauth.jwt.clientSecret}")
	private String clientSecret;

	@Value("${oauth.jwt.signingKey}")
	private String signingKey;

	@Value("${oauth.jwt.tokenValiditySeconds}")
	private int accessTokenValiditySeconds;

	@Value("${oauth.jwt.refreshTokenValiditySeconds}")
	private int refreshTokenValiditySeconds;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient(clientId)
				.secret(passwordEncoder.encode(clientSecret))
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
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
		accessTokenConverter.setSigningKey(signingKey);
		return accessTokenConverter;
	}
}
