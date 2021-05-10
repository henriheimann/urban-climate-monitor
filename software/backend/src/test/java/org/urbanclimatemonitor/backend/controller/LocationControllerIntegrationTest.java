package org.urbanclimatemonitor.backend.controller;

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

		this.mockMvc.perform(get("/location/%d".formatted(id))
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

		this.mockMvc.perform(delete("/location/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken()))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/location/%d".formatted(id))
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

		long iconUploadId = createUpload("icons", "prosperIV.png", MediaType.IMAGE_PNG);
		long model3dUploadId = createUpload("models", "cube2.obj", MediaType.TEXT_PLAIN);

		this.mockMvc.perform(put("/location/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Changed name",
					"icon": {
						"id": "%d"
					},
					"model3d": {
						"id": "%d"
					},
					"sensors": []
				}
				""".formatted(iconUploadId, model3dUploadId)))
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
		private int iconId;
		private int model3dId;
	}

	private LocationWithSensorsSetup setupLocationWithSensors() throws Exception
	{
		ttnWireMockConfig.setupApplicationsDevicesGet(ttnWireMockConfig.loadMockJson("applications-devices-get_with-influxdb-device.json"));

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
	        "dev_eui": "9CD67D0FEA2DC57E",
	        "app_id": "ucm-ttn-app-id",
	        "dev_id": "%1$s",
	        "dev_addr": "2601BB69",
	        "nwk_s_key": "90BEE95C75DEBADDA8AB8V91BFEE1238",
	        "app_s_key": "4E7C735867B1FB62768FD81942CFB51E",
	        "app_key": "",
	        "disable_f_cnt_check": true,
	        "uses32_bit_f_cnt": true,
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
		int locationIconId = JsonPath.read(locationContent, "$.icon.id");
		int locationModel3dId = JsonPath.read(locationContent, "$.model3d.id");

		this.mockMvc.perform(put("/sensor/%d".formatted(sensorId1))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Sensor Name 1",
					"locationId": %d
				}
				""".formatted(locationId)))
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/sensor/%d".formatted(sensorId2))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Sensor Name 2",
					"locationId": %d
				}
				""".formatted(locationId)))
				.andDo(log(log))
				.andExpect(status().isOk());

		return new LocationWithSensorsSetup(sensorId1, sensorId2, locationId, locationIconId, locationModel3dId);
	}

	@Test
	public void getLocation_returnsSensors() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/location/%d".formatted(setup.getLocationId()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#")))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Testlocation")))
				.andExpect(jsonPath("$.sensors", hasSize(2)))
				.andExpect(jsonPath("$.sensors[*].name", containsInAnyOrder("Sensor Name 1", "Sensor Name 2")))
				.andExpect(jsonPath("$.sensors[*].ttnId", containsInAnyOrder("device-001", "002-pcb-prototype")));

		// TODO: Verify measurements
	}

	@Test
	public void updateLocation_updatesSensor() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER", (long)setup.getLocationId())
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/location/%d".formatted(setup.getLocationId()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Changed Location Name",
					"icon": {
						"id": %d
					},
					"model3d": {
						"id": %d
					},
					"sensors": [
						{
							"id": %d,
							"name": "Changed Sensor Name 1",
							"position": [1.1, 2.0, 3.0],
							"rotation": [0.0, 4.4, 0.0]
						},
						{
							"id": %d,
							"name": "Changed Sensor Name 2",
							"position": [0.0, 0.0, 5.0],
							"rotation": [0.0, 6.4, 0.0]
						}
					]
				}""".formatted(setup.getIconId(), setup.getModel3dId(), setup.getSensorId1(), setup.getSensorId2())))
				.andDo(log(log))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Changed Location Name")))
				.andExpect(jsonPath("$.sensors", hasSize(2)))
				.andExpect(jsonPath("$.sensors[*].name", containsInAnyOrder("Changed Sensor Name 1", "Changed Sensor Name 2")));

		// TODO: Verify position and rotation changes
	}

	@Test
	public void updateLocation_failsForChangedSensorLocationMatching() throws Exception
	{
		LocationWithSensorsSetup setup = setupLocationWithSensors();

		createUser("user1", "testPassword1234#", "USER", (long)setup.getLocationId())
				.andDo(log(log))
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/location/%d".formatted(setup.getLocationId()))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Changed Location Name",
					"icon": {
						"id": %d
					},
					"model3d": {
						"id": %d
					},
					"sensors": [
						{
							"id": %d,
							"name": "Changed Sensor Name 2",
							"position": [0.0, 0.0, 5.0],
							"rotation": [0.0, 6.4, 0.0]
						}
					]
				}""".formatted(setup.getIconId(), setup.getModel3dId(), setup.getSensorId2())))
				.andDo(log(log))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateLocation_failsForUserWithoutPermission() throws Exception
	{
		createUser("user1", "testPassword1234#", "USER")
				.andDo(log(log))
				.andExpect(status().isOk());

		String content = createLocation("Testlocation", "prosperIII.png", "cube.obj")
				.andDo(log(log))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		int id = JsonPath.read(content, "$.id");

		long iconUploadId = createUpload("icons", "prosperIV.png", MediaType.IMAGE_PNG);
		long model3dUploadId = createUpload("models", "cube2.obj", MediaType.TEXT_PLAIN);

		this.mockMvc.perform(put("/location/%d".formatted(id))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken("user1", "testPassword1234#"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
				{
					"name": "Changed name",
					"icon": {
						"id": "%d"
					},
					"model3d": {
						"id": "%d"
					},
					"sensors": []
				}
				""".formatted(iconUploadId, model3dUploadId)))
				.andDo(log(log))
				.andExpect(status().isUnauthorized());
	}
}
