package org.urbanclimatemonitor.backend.ttn;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceDTO;

import java.util.List;

@Service
@Log4j2
public class TTNService
{
	@Value("${TTN_APP_ID}")
	private String applicationId;

	private final TTNClient ttnClient;

	public TTNService(TTNClient ttnClient)
	{
		this.ttnClient = ttnClient;
	}

	public void pollDevices()
	{
		List<TTNDeviceDTO> devices = ttnClient.getDevicesForApplication(applicationId).getDevices();
	}
}
