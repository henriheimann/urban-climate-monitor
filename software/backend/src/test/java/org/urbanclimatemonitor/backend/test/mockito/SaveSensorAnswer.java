package org.urbanclimatemonitor.backend.test.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.urbanclimatemonitor.backend.core.entities.Sensor;

public class SaveSensorAnswer implements Answer<Sensor>
{
	private final Long id;

	public SaveSensorAnswer(Long id)
	{
		this.id = id;
	}

	@Override
	public Sensor answer(InvocationOnMock invocationOnMock) throws Throwable
	{
		Sensor sensor = invocationOnMock.getArgument(0);
		if (sensor.getId() == null) {
			sensor.setId(id);
		}
		return sensor;
	}
}
