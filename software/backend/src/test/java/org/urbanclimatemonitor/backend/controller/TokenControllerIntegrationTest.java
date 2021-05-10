package org.urbanclimatemonitor.backend.controller;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.urbanclimatemonitor.backend.config.properties.OAuthConfigurationProperties;
import org.urbanclimatemonitor.backend.test.BaseIntegrationTest;
import org.urbanclimatemonitor.backend.test.mocks.TTNWireMockConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.urbanclimatemonitor.backend.test.logging.CustomMockMvcResultHandler.log;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = { TTNWireMockConfig.class })
@Log4j2
class TokenControllerIntegrationTest extends BaseIntegrationTest
{
	@Autowired
	private OAuthConfigurationProperties oAuthProperties;

	@Test
	public void getToken_success() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		String content = this.mockMvc.perform(post("/oauth/token")
				.queryParam("username", "user1")
				.queryParam("password", "testPassword1234#")
				.queryParam("grant_type", "password")
				.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String accessToken = JsonPath.read(content, "$.access_token");

		this.mockMvc.perform(get("/user/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
				.andDo(log(log))
				.andExpect(status().isOk());
	}

	@Test
	public void refreshTokenTwoTimes_success() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		String content = this.mockMvc.perform(post("/oauth/token")
				.queryParam("username", "user1")
				.queryParam("password", "testPassword1234#")
				.queryParam("grant_type", "password")
				.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String refreshToken = JsonPath.read(content, "$.refresh_token");

		content = this.mockMvc.perform(post("/oauth/token")
				.queryParam("refresh_token", refreshToken)
				.queryParam("grant_type", "refresh_token")
				.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String refreshedAccessToken = JsonPath.read(content, "$.access_token");
		refreshToken = JsonPath.read(content, "$.refresh_token");

		this.mockMvc.perform(get("/user/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshedAccessToken))
				.andDo(log(log))
				.andExpect(status().isOk());

		content = this.mockMvc.perform(post("/oauth/token")
				.queryParam("refresh_token", refreshToken)
				.queryParam("grant_type", "refresh_token")
				.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		refreshedAccessToken = JsonPath.read(content, "$.access_token");

		this.mockMvc.perform(get("/user/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshedAccessToken))
				.andDo(log(log))
				.andExpect(status().isOk());
	}

	@Test
	public void revokeToken_success() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		String content = this.mockMvc.perform(post("/oauth/token")
				.queryParam("username", "user1")
				.queryParam("password", "testPassword1234#")
				.queryParam("grant_type", "password")
				.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String accessToken = JsonPath.read(content, "$.access_token");
		String refreshToken = JsonPath.read(content, "$.refresh_token");

		this.mockMvc.perform(post("/oauth/revoke")
				.queryParam("token", accessToken))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/user/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
				.andDo(log(log))
				.andExpect(status().isUnauthorized());

		this.mockMvc.perform(post("/oauth/token")
				.queryParam("refresh_token", refreshToken)
				.queryParam("grant_type", "refresh_token")
				.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
				.andDo(log(log))
				.andExpect(status().isBadRequest());
	}
}