package org.urbanclimatemonitor.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info = @Info(title = "Urban Climate Monitor API", version = "v1"),
		servers = {
				@Server(url = "http://localhost:8080", description = "Local Development Environment"),
				@Server(url = "http://localhost/api", description = "Local Test Environment"),
				@Server(url = "https://www.urbanclimatemonitor.org/api", description = "Production")
		}
)
@SecurityScheme(
		name = "auth",
		type = SecuritySchemeType.OAUTH2,
		in = SecuritySchemeIn.HEADER,
		bearerFormat = "jwt",
		flows = @OAuthFlows(
				password = @OAuthFlow(
						tokenUrl = "/oauth/token"
				)
		)
)
public class OpenAPIConfiguration
{
}
