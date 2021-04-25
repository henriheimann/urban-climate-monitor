package org.urbanclimatemonitor.backend.core.controller;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;
import org.urbanclimatemonitor.backend.test.BaseIntegrationTest;
import org.urbanclimatemonitor.backend.test.mocks.TTNWireMockConfig;
import org.urbanclimatemonitor.backend.ttn.TTNService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.urbanclimatemonitor.backend.test.logging.CustomMockMvcResultHandler.log;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = { TTNWireMockConfig.class })
@Log4j2
public class SensorControllerIntegrationTest extends BaseIntegrationTest
{
	@Autowired
	TTNWireMockConfig ttnWireMockConfig;

	@Autowired
	SensorRepository sensorRepository;

	@Autowired
	TTNService ttnService;

	@Test
	public void getAllSensors_succeeds() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesGet(ttnWireMockConfig.loadMockJson("applications-devices-get.json"));

		this.mockMvc.perform(get("/sensors")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[*].ttnId", containsInAnyOrder("device-001", "device-002")))
				.andExpect(jsonPath("$[*].name", containsInAnyOrder("Unnamed", "Unnamed")))
				.andExpect(jsonPath("$[*].locationId", containsInAnyOrder(IsNull.nullValue(), IsNull.nullValue())));
	}

	@Test
	public void getAllSensors_removesSensorIfRemovedInTTN() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesGet(ttnWireMockConfig.loadMockJson("applications-devices-get.json"));

		this.mockMvc.perform(get("/sensors")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk());

		ttnService.ttnDevicesCacheEvict();

		ttnWireMockConfig.setupApplicationsDevicesGet(
				ttnWireMockConfig.loadMockJson("applications-devices-get_removed-device.json"));

		this.mockMvc.perform(get("/sensors")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[*].ttnId", containsInAnyOrder("device-001")))
				.andExpect(jsonPath("$[*].name", containsInAnyOrder("Unnamed")))
				.andExpect(status().isOk());
	}

	@Test
	public void createSensor_succeeds() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesPost();

		createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isOk());
	}

	@Test
	public void createSensor_failsAndRollsBackIfTTNRequestFails() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesGet(ttnWireMockConfig.loadMockJson("applications-devices-get.json"));
		ttnWireMockConfig.setupApplicationsDevicesPost_unauthorized();

		createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isInternalServerError());

		this.mockMvc.perform(get("/sensors")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(jsonPath("$[*].name", not(containsInAnyOrder("Testsensor"))))
				.andExpect(status().isOk());
	}

	@Test
	public void getSensor_succeeds() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesPost();

		String content = createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Testsensor")))
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");
		String ttnDeviceId = JsonPath.read(content, "$.ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet(ttnDeviceId, """
				{
				      "app_id": "ucm-ttn-app-id",
				      "dev_id": "%1$s",
				      "lorawan_device": {
				        "app_eui": "2D42890323AC1323",
				        "dev_eui": "D9A5D8C71C10D21A",
				        "app_id": "ucm-ttn-app-id",
				        "dev_id": "%1$s",
				        "dev_addr": "260157EE",
				        "nwk_s_key": "570B4D15DDCFD28D2FFC4B90DEA48d2D",
				        "app_s_key": "5E84BB636C7082BFC37EFFF250A0F620",
				        "app_key": "",
				        "disable_f_cnt_check": true,
				        "activation_constraints": "local"
				      }
				    }
				""".formatted(ttnDeviceId));

		this.mockMvc.perform(get("/sensor/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.ttnId", is(ttnDeviceId)))
				.andExpect(jsonPath("$.name", is("Testsensor")));
	}

	@Test
	public void getSensor_failsAndDeletesSensorIfTTNDeviceNotFound() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesPost();

		String content = createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");
		String ttnDeviceId = JsonPath.read(content, "$.ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet_notFound(ttnDeviceId);

		this.mockMvc.perform(get("/sensor/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteSensor_succeeds() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesPost();

		String content = createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");
		String ttnDeviceId = JsonPath.read(content, "$.ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet(ttnDeviceId, """
				{
				      "app_id": "ucm-ttn-app-id",
				      "dev_id": "%1$s",
				      "lorawan_device": {
				        "app_eui": "2D42890323AC1323",
				        "dev_eui": "D9A5D8C71C10D21A",
				        "app_id": "ucm-ttn-app-id",
				        "dev_id": "%1$s",
				        "dev_addr": "260157EE",
				        "nwk_s_key": "570B4D15DDCFD28D2FFC4B90DEA48d2D",
				        "app_s_key": "5E84BB636C7082BFC37EFFF250A0F620",
				        "app_key": "",
				        "disable_f_cnt_check": true,
				        "activation_constraints": "local"
				      }
				    }
				""".formatted(ttnDeviceId));
		ttnWireMockConfig.setupApplicationsDevicesIdDelete(ttnDeviceId);

		this.mockMvc.perform(delete("/sensor/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/sensor/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateSensor_succeeds() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesPost();

		String content = createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");
		String ttnDeviceId = JsonPath.read(content, "$.ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet(ttnDeviceId, """
				{
				      "app_id": "ucm-ttn-app-id",
				      "dev_id": "%1$s",
				      "lorawan_device": {
				        "app_eui": "2D42890323AC1323",
				        "dev_eui": "D9A5D8C71C10D21A",
				        "app_id": "ucm-ttn-app-id",
				        "dev_id": "%1$s",
				        "dev_addr": "260157EE",
				        "nwk_s_key": "570B4D15DDCFD28D2FFC4B90DEA48d2D",
				        "app_s_key": "5E84BB636C7082BFC37EFFF250A0F620",
				        "app_key": "",
				        "disable_f_cnt_check": true,
				        "activation_constraints": "local"
				      }
				    }
				""".formatted(ttnDeviceId));

		this.mockMvc.perform(put("/sensor/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "New Testsensor"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.ttnId", is(ttnDeviceId)))
				.andExpect(jsonPath("$.name", is("New Testsensor")));

		this.mockMvc.perform(get("/sensor/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.ttnId", is(ttnDeviceId)))
				.andExpect(jsonPath("$.name", is("New Testsensor")));
	}

	@Test
	public void getSensorKeys_succeeds() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesPost();

		String content = createSensor("Testsensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Testsensor")))
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");
		String ttnDeviceId = JsonPath.read(content, "$.ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet(ttnDeviceId, """
				{
				      "app_id": "ucm-ttn-app-id",
				      "dev_id": "%1$s",
				      "lorawan_device": {
				        "app_eui": "2D42890323AC1323",
				        "dev_eui": "D9A5D8C71C10D21A",
				        "app_id": "ucm-ttn-app-id",
				        "dev_id": "%1$s",
				        "dev_addr": "260157EE",
				        "nwk_s_key": "570B4D15DDCFD28D2FFC4B90DEA48d2D",
				        "app_s_key": "5E84BB636C7082BFC37EFFF250A0F620",
				        "app_key": "",
				        "disable_f_cnt_check": true,
				        "activation_constraints": "local"
				      }
				    }
				""".formatted(ttnDeviceId));

		this.mockMvc.perform(get("/sensor/%d/keys".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.deviceAddress", is("260157EE")))
				.andExpect(jsonPath("$.applicationSessionKey", is("5E84BB636C7082BFC37EFFF250A0F620")))
				.andExpect(jsonPath("$.networkSessionKey", is("570B4D15DDCFD28D2FFC4B90DEA48d2D")));
	}
}
