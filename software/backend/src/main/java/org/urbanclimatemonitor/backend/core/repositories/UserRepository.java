package org.urbanclimatemonitor.backend.core.repositories;

import org.springframework.data.repository.CrudRepository;
import org.urbanclimatemonitor.backend.core.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>
{
	Optional<User> findByEmail(String email);
}
