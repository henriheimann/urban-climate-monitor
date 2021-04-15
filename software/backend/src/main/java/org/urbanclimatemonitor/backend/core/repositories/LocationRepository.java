package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.core.entities.Location;

public interface LocationRepository extends CrudRepository<Location, Long>
{
}
