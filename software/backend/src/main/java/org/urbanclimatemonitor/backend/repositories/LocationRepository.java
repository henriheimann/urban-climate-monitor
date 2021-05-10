package org.urbanclimatemonitor.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.entities.Location;

public interface LocationRepository extends CrudRepository<Location, Long>
{
}
