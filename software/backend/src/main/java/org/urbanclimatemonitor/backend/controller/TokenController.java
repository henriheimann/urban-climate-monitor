package org.urbanclimatemonitor.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "oauth", description = "Additional OAuth2 API")
@RestController
public class TokenController
{
	private final TokenStore tokenStore;

	public TokenController(TokenStore tokenStore)
	{
		this.tokenStore = tokenStore;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/oauth/revoke")
	public void revokeToken(@RequestParam String token)
	{
		OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
		if (accessToken != null) {
			this.tokenStore.removeAccessToken(accessToken);

			OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
			if (refreshToken != null) {
				this.tokenStore.removeRefreshToken(refreshToken);
			}
		}
	}
}
