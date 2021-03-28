package org.urbanclimatemonitor.backend.core.controller;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.urbanclimatemonitor.backend.test.BaseIntegrationTest;
import org.urbanclimatemonitor.backend.test.mocks.TTNWireMockConfig;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.urbanclimatemonitor.backend.test.logging.CustomMockMvcResultHandler.log;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = { TTNWireMockConfig.class })
@Log4j2
public class LocationControllerIntegrationTest extends BaseIntegrationTest
{
	@Autowired
	TTNWireMockConfig ttnWireMockConfig;

	@Test
	public void createLocation_succeeds() throws Exception
	{
		createLocation("testlocation", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("testlocation")))
				.andExpect(jsonPath("$.icon.filename", is("prosperIII.png")))
				.andExpect(jsonPath("$.model3d.filename", is("cube.obj")));
	}

	@Test
	public void getAllLocationsAsUser_succeeds() throws Exception
	{
		createLocation("testlocation1", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk());

		createLocation("testlocation2", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk());

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/locations")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].name", containsInAnyOrder("testlocation1", "testlocation2")));
	}

	@Test
	public void getLocationAsUser_succeeds() throws Exception
	{
		String content = createLocation("testlocation1", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/locations/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("testlocation1")));
	}

	@Test
	public void deleteLocation_succeeds() throws Exception
	{
		String content = createLocation("testlocation1", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");

		this.mockMvc.perform(delete("/locations/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/locations/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateLocation_succeeds() throws Exception
	{
		String content = createLocation("Testlocation", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");

		this.mockMvc.perform(put("/locations/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Changed name",
					"icon": {
						"filename": "prosperIV.png",
						"data": "%s"
					},
					"model3d": {
						"filename": "cube2.obj",
						"data": "%s"
					}
				}
				""".formatted(loadUploadBase64Encoded("icons", "prosperIII.png"),
						loadUploadBase64Encoded("models", "cube.obj"))))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Changed name")))
				.andExpect(jsonPath("$.icon.filename", is("prosperIV.png")))
				.andExpect(jsonPath("$.model3d.filename", is("cube2.obj")));
	}

	@Data
	@AllArgsConstructor
	private static class LocationWithSensorsSetup
	{
		private int sensorId1;
		private int sensorId2;
		private int locationId;
	}

	private LocationWithSensorsSetup setupLocationWithSensors() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesGet(ttnWireMockConfig.loadMockJson("applications-devices-get.json"));

		String sensorsContent = this.mockMvc.perform(get("/sensors")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andReturn()
				.getResponse()
				.getContentAsString();

		int sensorId1 = JsonPath.read(sensorsContent, "$[0].id");
		String ttnDeviceId1 = JsonPath.read(sensorsContent, "$[0].ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet(ttnDeviceId1, """
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
				""".formatted(ttnDeviceId1));

		int sensorId2 = JsonPath.read(sensorsContent, "$[1].id");
		String ttnDeviceId2 = JsonPath.read(sensorsContent, "$[1].ttnId");

		ttnWireMockConfig.setupApplicationsDevicesIdGet(ttnDeviceId2, """
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
				""".formatted(ttnDeviceId2));

		String locationContent = createLocation("Testlocation", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int locationId = JsonPath.read(locationContent, "$.id");

		this.mockMvc.perform(put("/sensors/%d".formatted(sensorId1))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Sensor Name 1",
					"location": %d
				}
				""".formatted(locationId)))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/sensors/%d".formatted(sensorId2))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Sensor Name 2",
					"location": %d
				}
				""".formatted(locationId)))
				.andDo(log(log))
				.andExpect(status().isOk());

		return new LocationWithSensorsSetup(sensorId1, sensorId2, locationId);
	}

	@Test
	public void getLocationSensors_succeeds() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/locations/%d/sensors".formatted(setup.getLocationId()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].name", containsInAnyOrder("Sensor Name 1", "Sensor Name 2")));
	}

	@Test
	public void getLocationSensor_succeeds() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/locations/%d/sensors/%d".formatted(setup.getLocationId(), setup.getSensorId1()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Sensor Name 1")));
	}

	@Test
	public void getLocationSensor_failsForSensorWithOtherLocation() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		ttnWireMockConfig.setupApplicationsDevicesPost();

		String sensorsContent = createSensor("Other sensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int otherSensorId = JsonPath.read(sensorsContent, "$.id");

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/locations/%d/sensors/%d".formatted(setup.getLocationId(), otherSensorId))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateLocationSensor_success() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER", (long)setup.getLocationId())
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/locations/%d/sensors/%d".formatted(setup.getLocationId(), setup.getSensorId1()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "New Sensor Name"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Matchers.is("New Sensor Name")));
	}

	@Test
	public void updateLocationSensor_failsForSensorWithOtherLocation() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		ttnWireMockConfig.setupApplicationsDevicesPost();

		String sensorsContent = createSensor("Other sensor")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int otherSensorId = JsonPath.read(sensorsContent, "$.id");

		createUser("user1", "testPassword1234#", "USER", (long)setup.getLocationId())
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/locations/%d/sensors/%d".formatted(setup.getLocationId(), otherSensorId))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "New Sensor Name"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateLocationSensor_failsForUserWithoutPermission() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/locations/%d/sensors/%d".formatted(setup.getLocationId(), setup.getSensorId1()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "New Sensor Name"
				}
				"""))
				.andDo(log(log))
				.andExpect(status().isUnauthorized());
	}
}
