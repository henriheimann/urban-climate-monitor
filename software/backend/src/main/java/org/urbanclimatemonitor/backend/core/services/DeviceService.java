package org.urbanclimatemonitor.backend.core.services;

import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.core.dto.DeviceDTO;
import org.urbanclimatemonitor.backend.core.repositories.DeviceRepository;
import org.urbanclimatemonitor.backend.core.util.Streams;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService
{
	private final DeviceRepository deviceRepository;

	public DeviceService(DeviceRepository deviceRepository)
	{
		this.deviceRepository = deviceRepository;
	}

	public List<DeviceDTO> loadAllDevices()
	{
		return Streams.stream(deviceRepository.findAll())
				.map(device -> new DeviceDTO(device.getId(), device.getLocation().getId()))
				.collect(Collectors.toList());
	}
}
