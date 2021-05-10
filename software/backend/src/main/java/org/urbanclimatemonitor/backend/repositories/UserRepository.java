package org.urbanclimatemonitor.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.entities.User;

public interface UserRepository extends CrudRepository<User, String>
{

}
