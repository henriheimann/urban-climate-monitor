package org.urbanclimatemonitor.backend.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.urbanclimatemonitor.backend.security.ManageableJwtTokenStore;

@Controller
public class TokenController
{
	private final ManageableJwtTokenStore tokenStore;

	public TokenController(ManageableJwtTokenStore tokenStore)
	{
		this.tokenStore = tokenStore;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/oauth/revoke")
	public void revokeToken(@RequestParam String token)
	{
		OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(token);
		if (refreshToken != null) {
			this.tokenStore.removeRefreshToken(refreshToken);
		}
	}
}
