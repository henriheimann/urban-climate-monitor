package org.urbanclimatemonitor.backend.integration;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.urbanclimatemonitor.backend.config.properties.InitialisationConfigurationProperties;
import org.urbanclimatemonitor.backend.config.properties.OAuthConfigurationProperties;
import org.urbanclimatemonitor.backend.core.dto.request.CreateUserDTO;
import org.urbanclimatemonitor.backend.core.entities.Role;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.core.repositories.UserRepository;
import org.urbanclimatemonitor.backend.core.services.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.urbanclimatemonitor.backend.test.logging.CustomMockMvcResultHandler.log;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class BaseIntegrationTest
{
	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	private OAuthConfigurationProperties oAuthProperties;

	@Autowired
	private InitialisationConfigurationProperties initialisationProperties;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SensorRepository sensorRepository;

	@Autowired
	UserService userService;

	private Map<String, String> tokenCache;

	@BeforeEach
	public void cleanTokenCache()
	{
		tokenCache = new HashMap<>();
	}

	@BeforeEach
	public void setupDatabase()
	{
		userService.createUser(new CreateUserDTO(initialisationProperties.getAdmin().getUsername(),
				initialisationProperties.getAdmin().getPassword(), Role.ADMIN));
	}

	@AfterEach
	public void cleanDatabase()
	{
		userRepository.deleteAll();
		sensorRepository.deleteAll();
	}

	protected void clearTokenForUsername(String username)
	{
		tokenCache.remove(username);
	}

	protected String getAdminToken() throws Exception
	{
		return getUserToken(initialisationProperties.getAdmin().getUsername(),
				initialisationProperties.getAdmin().getPassword());
	}

	protected String getUserToken(String username, String password) throws Exception
	{
		if (tokenCache.get(username) == null) {
			String content = this.mockMvc.perform(post("/oauth/token")
					.queryParam("username", username)
					.queryParam("password", password)
					.queryParam("grant_type", "password")
					.with(httpBasic(oAuthProperties.getJwt().getClientId(), oAuthProperties.getJwt().getClientSecret())))
					.andDo(log(log))
					.andExpect(status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
			tokenCache.put(username, JsonPath.read(content, "$.access_token"));
		}

		return tokenCache.get(username);
	}

	protected ResultActions createUser(String username, String password, String role, Long... locationsWithPermission)
			throws Exception
	{
		return this.mockMvc.perform(post("/users")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"username": "%s",
					"password": "%s",
					"role": "%s",
					"locationsWithPermission": %s
				}
				""".formatted(username, password, role, Arrays.stream(locationsWithPermission)
						.map(id -> Long.toString(id))
						.collect(Collectors.joining(", ", "[", "]"))
				))
		);
	}

	protected ResultActions createSensor(String name) throws Exception
	{
		return this.mockMvc.perform(post("/sensors")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "%s"
				}
				""".formatted(name)));
	}
}