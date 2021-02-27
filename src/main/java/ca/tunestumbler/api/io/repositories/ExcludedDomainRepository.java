package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.ExcludedDomainEntity;

public interface ExcludedDomainRepository extends JpaRepository<ExcludedDomainEntity, String> {
	@Query(value = "(SELECT * FROM excluded_domains WHERE excluded_domain_id in :excludedDomainIds AND user_id = :userId) ORDER BY id ASC;", nativeQuery = true)
	List<ExcludedDomainEntity> findAllExcludedDomainsByExcludedDomainIdsAndUserId(
			@Param("excludedDomainIds") List<String> excludedDomainIds, @Param("userId") String userId);
}
