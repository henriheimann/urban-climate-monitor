package org.urbanclimatemonitor.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.urbanclimatemonitor.backend.config.properties.OAuthConfigurationProperties;
import org.urbanclimatemonitor.backend.security.exception.CustomWebResponseExceptionTranslator;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	private final UserDetailsService userService;

	private final CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

	private final OAuthConfigurationProperties properties;

	private final DataSource dataSource;

	public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
	                                        PasswordEncoder passwordEncoder, CustomUserDetailsService userService,
	                                        CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator,
	                                        OAuthConfigurationProperties properties, DataSource dataSource)
	{
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.customWebResponseExceptionTranslator = customWebResponseExceptionTranslator;
		this.properties = properties;
		this.dataSource = dataSource;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient(properties.getClientId())
				.secret(passwordEncoder.encode(properties.getClientSecret()))
				.accessTokenValiditySeconds(properties.getTokenValiditySeconds())
				.refreshTokenValiditySeconds(properties.getRefreshTokenValiditySeconds())
				.authorizedGrantTypes("password", "refresh_token")
				.scopes("all");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
	{
		endpoints.tokenServices(tokenServices())
				.userDetailsService(userService)
				.authenticationManager(authenticationManager)
				.exceptionTranslator(customWebResponseExceptionTranslator);
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices()
	{
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Bean
	public TokenStore tokenStore()
	{
		return new JdbcTokenStore(dataSource);
	}
}
