package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.core.entities.Device;

public interface DeviceRepository extends CrudRepository<Device, String>
{
}
