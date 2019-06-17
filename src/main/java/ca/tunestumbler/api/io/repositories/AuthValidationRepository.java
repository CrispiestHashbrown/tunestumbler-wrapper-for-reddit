package ca.tunestumbler.api.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.tunestumbler.api.io.entity.AuthValidationEntity;

@Repository
public interface AuthValidationRepository extends CrudRepository<AuthValidationEntity, String> {
	AuthValidationEntity findByStateId(String stateId);

	AuthValidationEntity findByStateIdAndValidated(String stateId, Boolean validated);

	AuthValidationEntity findByUserEntity(@Param("userId") String userId);
}
