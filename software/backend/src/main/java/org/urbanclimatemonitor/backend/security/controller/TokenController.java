package org.urbanclimatemonitor.backend.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.urbanclimatemonitor.backend.security.ManageableJwtTokenStore;

@Tag(name = "oauth", description = "Additional OAuth2 API")
@RestController
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
