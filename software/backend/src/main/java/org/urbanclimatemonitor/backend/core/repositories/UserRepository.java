package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.core.entities.User;

public interface UserRepository extends CrudRepository<User, String>
{

}
