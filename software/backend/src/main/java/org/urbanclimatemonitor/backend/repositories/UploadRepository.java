package org.urbanclimatemonitor.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.entities.Upload;

public interface UploadRepository extends CrudRepository<Upload, Long>
{
}
