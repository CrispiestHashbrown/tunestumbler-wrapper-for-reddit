package ca.tunestumbler.api;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.tunestumbler.api.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
}
