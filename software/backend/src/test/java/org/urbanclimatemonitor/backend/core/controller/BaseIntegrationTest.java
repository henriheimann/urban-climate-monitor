package org.urbanclimatemonitor.backend.core.controller;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseIntegrationTest
{
	@Autowired
	protected MockMvc mockMvc;

	protected String getAdminToken() throws Exception
	{
		String content = this.mockMvc.perform(post("/oauth/token")
				.queryParam("username", "admin@urbanclimatemonitor.org")
				.queryParam("password", "testPassword1234#")
				.queryParam("grant_type", "password")
				.with(httpBasic("ucm-client", "ucm-client-secret")))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return JsonPath.read(content, "$.access_token");
	}
}
