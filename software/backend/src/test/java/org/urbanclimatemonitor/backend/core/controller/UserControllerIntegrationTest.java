package org.urbanclimatemonitor.backend.core.controller;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.urbanclimatemonitor.backend.test.BaseIntegrationTest;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.urbanclimatemonitor.backend.test.logging.CustomMockMvcResultHandler.log;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class UserControllerIntegrationTest extends BaseIntegrationTest
{
	@Test
	public void createUser_succeeds() throws Exception
	{
		String content = createLocation("testlocation", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int locationId = JsonPath.read(content, "$.id");

		createUser("user", "testPassword1234#", "USER", (long)locationId)
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("user")))
				.andExpect(jsonPath("$.role", is("USER")))
				.andExpect(jsonPath("$.locationsWithPermission[*]", containsInAnyOrder(locationId)));
	}

	@Test
	public void getAllUsers_succeeds() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		createUser("admin1", "testPassword5678#", "ADMIN")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/users")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].username", containsInAnyOrder("admin", "user1", "admin1")));
	}

	@Test
	public void getUserAsAdmin_succeeds() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/users/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("user1")))
				.andExpect(jsonPath("$.role", is("USER")));
	}

	@Test
	public void getUserAsSelf_succeeds() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/users/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("user1")))
				.andExpect(jsonPath("$.role", is("USER")));
	}

	@Test
	public void getUserAsOtherUser_fails() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		createUser("user2", "testPassword5678#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/users/user")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isForbidden());
	}

	@Test
	public void deleteUser_succeeds() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(delete("/users/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/users/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateUser_succeeds() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/users/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"role": "ADMIN",
					"locationsWithPermission": []
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("user1")))
				.andExpect(jsonPath("$.role", is("ADMIN")));
	}

	@Test
	public void updateUserPassword_succeeds() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(post("/users/user1/password")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"oldPassword": "testPassword1234#",
					"newPassword": "newPassword5678!"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isOk());

		clearTokenForUsername("user1");

		this.mockMvc.perform(get("/users/user1")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "newPassword5678!")))
				.andDo(log(log))
				.andExpect(status().isOk());
	}

	@Test
	public void updateUserPasswordWithWrongOldPasswords_fails() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(post("/users/user1/password")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"oldPassword": "wrong",
					"newPassword": "newPassword5678!"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateUserPasswordAsOtherUser_fails() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		createUser("user2", "testPassword5678#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(post("/users/user1/password")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user2", "testPassword5678#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"oldPassword": "testPassword1234#",
					"newPassword": "newPassword5678!"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isForbidden());
	}
}
