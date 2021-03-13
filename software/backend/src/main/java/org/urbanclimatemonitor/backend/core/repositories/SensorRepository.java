package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.core.entities.Sensor;

public interface SensorRepository extends CrudRepository<Sensor, Long>
{
}
