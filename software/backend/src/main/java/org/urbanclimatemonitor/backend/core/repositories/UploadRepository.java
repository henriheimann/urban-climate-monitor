package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.core.entities.Upload;

public interface UploadRepository extends CrudRepository<Upload, Long>
{
}
