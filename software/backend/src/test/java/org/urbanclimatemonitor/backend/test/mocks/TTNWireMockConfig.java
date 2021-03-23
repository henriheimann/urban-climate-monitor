package org.urbanclimatemonitor.backend.test.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@TestConfiguration
public class TTNWireMockConfig
{
	@Autowired
	private WireMockServer ttnWireMockServer;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public WireMockServer ttnWireMockServer()
	{
		return new WireMockServer(8094);
	}

	public String loadMockJson(String filename) throws IOException
	{
		return copyToString(TTNWireMockConfig.class.getClassLoader().getResourceAsStream("mocks/ttn/" + filename),
				defaultCharset());
	}

	public void setupApplicationsDevicesGet(String content)
	{
		ttnWireMockServer.stubFor(get(urlEqualTo("/applications/ucm-ttn-app-id/devices"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody(content)));
	}

	public void setupApplicationsDevicesPost()
	{
		ttnWireMockServer.stubFor(post(urlEqualTo("/applications/ucm-ttn-app-id/devices"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody("{}")));
	}

	public void setupApplicationsDevicesPost_unauthorized()
	{
		ttnWireMockServer.stubFor(post(urlEqualTo("/applications/ucm-ttn-app-id/devices"))
				.willReturn(aResponse().withStatus(HttpStatus.UNAUTHORIZED.value())));
	}

	public void setupApplicationsDevicesIdGet(String deviceId, String content)
	{
		ttnWireMockServer.stubFor(get(urlEqualTo("/applications/ucm-ttn-app-id/devices/%s".formatted(deviceId)))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody(content)));
	}

	public void setupApplicationsDevicesIdGet_notFound(String deviceId)
	{
		ttnWireMockServer.stubFor(get(urlEqualTo("/applications/ucm-ttn-app-id/devices/%s".formatted(deviceId)))
				.willReturn(aResponse().withStatus(HttpStatus.NOT_FOUND.value())));
	}

	public void setupApplicationsDevicesIdDelete(String deviceId)
	{
		ttnWireMockServer.stubFor(delete(urlEqualTo("/applications/ucm-ttn-app-id/devices/%s".formatted(deviceId)))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())));
	}
}