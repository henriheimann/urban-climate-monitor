package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.urbanclimatemonitor.backend.core.entities.Sensor;

import java.util.Optional;

public interface SensorRepository extends CrudRepository<Sensor, Long>
{
	Optional<Sensor> findByTtnId(String ttnId);


}
