package ca.tunestumbler.api.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.tunestumbler.api.io.entity.AuthValidationEntity;

@Repository
public interface AuthValidationRepository extends CrudRepository<AuthValidationEntity, String> {
	AuthValidationEntity findByStateIdAndValidated(String stateId, Boolean validated);
}
