package org.urbanclimatemonitor.backend.test;

import lombok.experimental.UtilityClass;
import org.urbanclimatemonitor.backend.core.entities.Location;
import org.urbanclimatemonitor.backend.core.entities.Sensor;
import org.urbanclimatemonitor.backend.ttn.dto.TTNDeviceDTO;

@UtilityClass
public class TestEntities
{
	public Sensor sensor(Long id, String ttnId)
	{
		Sensor sensor = new Sensor();
		sensor.setId(id);
		sensor.setTtnId(ttnId);
		return sensor;
	}

	public Sensor sensor(Long id, String ttnId, String name)
	{
		Sensor sensor = new Sensor();
		sensor.setId(id);
		sensor.setTtnId(ttnId);
		sensor.setName(name);
		return sensor;
	}

	public Location location(Long id, String name)
	{
		Location location = new Location();
		location.setId(id);
		location.setName(name);
		return location;
	}

	public TTNDeviceDTO ttnDeviceDTO(String id)
	{
		TTNDeviceDTO ttnDeviceDTO = new TTNDeviceDTO();
		ttnDeviceDTO.setDeviceId(id);
		return ttnDeviceDTO;
	}
}
