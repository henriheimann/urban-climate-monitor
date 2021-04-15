package org.urbanclimatemonitor.backend.security;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public class ManageableJwtTokenStore extends JwtTokenStore
{
	private final TokenStore tokenStore;

	public ManageableJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer, TokenStore tokenStore)
	{
		super(jwtTokenEnhancer);
		this.tokenStore = tokenStore;
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue)
	{
		return tokenStore.readRefreshToken(tokenValue);
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication)
	{
		tokenStore.storeRefreshToken(refreshToken, authentication);
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token)
	{
		tokenStore.removeRefreshToken(token);
	}
}
