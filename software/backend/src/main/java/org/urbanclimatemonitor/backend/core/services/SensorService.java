package org.urbanclimatemonitor.backend.core.services;

import org.springframework.stereotype.Service;
import org.urbanclimatemonitor.backend.core.dto.SensorDTO;
import org.urbanclimatemonitor.backend.core.repositories.SensorRepository;

import java.util.List;

@Service
public class SensorService
{
	private final SensorRepository sensorRepository;

	public SensorService(SensorRepository sensorRepository)
	{
		this.sensorRepository = sensorRepository;
	}

	public List<SensorDTO> loadAllDevices()
	{
		return null;
	}
}
