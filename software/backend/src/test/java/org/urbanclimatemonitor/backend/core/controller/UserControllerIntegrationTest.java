package org.urbanclimatemonitor.backend.core.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest extends BaseIntegrationTest
{
	@Test
	public void testCreateUser() throws Exception
	{
		this.mockMvc.perform(post("/users")
				.header(HttpHeaders.AUTHORIZATION, "Bearer" + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"email": "user@urbanclimatemonitor.org",
					"password": "testPassword1234#",
					"role": "USER"
				}
				"""))
				.andExpect(status().isOk());
	}
}
