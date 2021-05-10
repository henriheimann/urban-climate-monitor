package org.urbanclimatemonitor.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.entities.Sensor;

import java.util.Optional;

public interface SensorRepository extends CrudRepository<Sensor, Long>
{
	Optional<Sensor> findByTtnId(String ttnId);


}
