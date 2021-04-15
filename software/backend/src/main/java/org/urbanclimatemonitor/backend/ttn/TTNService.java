package org.urbanclimatemonitor.backend.ttn;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.config.properties.TTNConfigurationProperties;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceDTO;
import org.urbanclimatemonitor.backend.ttn.dto.TTNLorawanDeviceDTO;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TTNService
{
	private final TTNConfigurationProperties properties;

	private final TTNClient ttnClient;

	public TTNService(TTNConfigurationProperties properties, TTNClient ttnClient)
	{
		this.properties = properties;
		this.ttnClient = ttnClient;
	}

	private static String generateRandomHexString(int length)
	{
		StringBuilder stringBuilder = new StringBuilder();

		SecureRandom secureRandom = new SecureRandom();
		secureRandom.ints(0, 16)
				.limit(length)
				.mapToObj("0123456789ABCDEF"::charAt)
				.forEach(stringBuilder::append);

		return stringBuilder.toString();
	}

	@CacheEvict(value="ttnDevices", allEntries=true)
	@Scheduled(fixedDelay = 60 * 1000)
	public void ttnDevicesCacheEvict() {

	}

	@Cacheable("ttnDevices")
	public List<TTNDeviceDTO> getAllDevices()
	{
		return Optional.ofNullable(ttnClient.getDevicesForApplication(properties.getAppId()).getDevices())
				.orElse(List.of());
	}

	@Cacheable("ttnDevices")
	public Optional<TTNDeviceDTO> getDevice(String deviceId)
	{
		try {
			return Optional.ofNullable(ttnClient.getDevice(properties.getAppId(), deviceId));
		} catch (FeignException e) {
			if (e.status() == HttpStatus.NOT_FOUND.value()) {
				return Optional.empty();
			} else {
				throw e;
			}
		}
	}

	@CacheEvict(value="ttnDevices", allEntries=true)
	public void deleteDevice(String deviceId)
	{
		ttnClient.deleteDevice(properties.getAppId(), deviceId);
	}

	@CacheEvict(value="ttnDevices", allEntries=true)
	public void createDevice(String deviceId)
	{
		TTNLorawanDeviceDTO lorawanDevice = new TTNLorawanDeviceDTO();
		lorawanDevice.setApplicationEui(properties.getAppEui());
		lorawanDevice.setApplicationId(properties.getAppId());
		lorawanDevice.setDeviceEui(generateRandomHexString(16));
		lorawanDevice.setDeviceId(deviceId);
		lorawanDevice.setDeviceAddress("2601" + generateRandomHexString(4));
		lorawanDevice.setNetworkSessionKey(generateRandomHexString(32));
		lorawanDevice.setApplicationSessionKey(generateRandomHexString(32));
		lorawanDevice.setDisableFrameCounterChecks(true);
		lorawanDevice.setUses32BitFrameCounter(true);
		lorawanDevice.setActivationConstraints("local");

		TTNDeviceDTO device = new TTNDeviceDTO();
		device.setDeviceId(deviceId);
		device.setApplicationId(properties.getAppId());
		device.setLorawanDevice(lorawanDevice);

		ttnClient.createDevice(properties.getAppId(), device);
	}
}
