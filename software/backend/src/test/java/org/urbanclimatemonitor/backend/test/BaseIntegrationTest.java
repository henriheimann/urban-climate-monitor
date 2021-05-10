package org.urbanclimatemonitor.backend.test;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.urbanclimatemonitor.backend.config.properties.InitialisationConfigurationProperties;
import org.urbanclimatemonitor.backend.config.properties.OAuthConfigurationProperties;
import org.urbanclimatemonitor.backend.controller.requests.CreateUserRequest;
import org.urbanclimatemonitor.backend.entities.Role;
import org.urbanclimatemonitor.backend.repositories.LocationRepository;
import org.urbanclimatemonitor.backend.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.repositories.UploadRepository;
import org.urbanclimatemonitor.backend.repositories.UserRepository;
import org.urbanclimatemonitor.backend.services.UserService;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StreamUtils.copyToByteArray;
import static org.springframework.util.StreamUtils.copyToString;
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
	LocationRepository locationRepository;

	@Autowired
	UploadRepository uploadRepository;

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
		userRepository.deleteAll();
		sensorRepository.deleteAll();
		locationRepository.deleteAll();
		uploadRepository.deleteAll();

		userService.createUser(new CreateUserRequest(initialisationProperties.getAdmin().getUsername(),
				initialisationProperties.getAdmin().getPassword(), Role.ADMIN));
	}

	@AfterEach
	public void cleanDatabase()
	{
		userRepository.deleteAll();
		sensorRepository.deleteAll();
		locationRepository.deleteAll();
		uploadRepository.deleteAll();
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
					.with(httpBasic(oAuthProperties.getClientId(), oAuthProperties.getClientSecret())))
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
		return this.mockMvc.perform(post("/user")
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
		return this.mockMvc.perform(post("/sensor")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "%s"
				}
				""".formatted(name)));
	}

	protected byte[] loadUpload(String folder, String filename) throws IOException
	{
		return copyToByteArray(BaseIntegrationTest.class.getClassLoader().getResourceAsStream(
				"uploads/" + folder + "/" + filename));
	}

	protected int createUpload(String folder, String filename, MediaType mediaType) throws Exception
	{
		MockMultipartFile file = new MockMultipartFile(
				"file",
				filename,
				mediaType.toString(),
				loadUpload(folder, filename)
		);

		String content = this.mockMvc.perform(multipart("/upload")
				.file(file)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return JsonPath.read(content, "$.id");
	}

	protected ResultActions createLocation(String name, String iconFilename, String model3dFilename) throws Exception
	{
		long iconUploadId = createUpload("icons", iconFilename, MediaType.IMAGE_PNG);
		long model3dUploadId = createUpload("models", model3dFilename, MediaType.TEXT_PLAIN);

		return this.mockMvc.perform(post("/location")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "%s",
					"icon": {
						"id": "%d"
					},
					"model3d": {
						"id": "%d"
					},
					"sensors": []
				}
				""".formatted(name, iconUploadId, model3dUploadId)));
	}
}
