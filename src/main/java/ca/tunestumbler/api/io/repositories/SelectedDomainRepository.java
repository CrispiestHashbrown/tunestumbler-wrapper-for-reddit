package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.SelectedDomainEntity;

public interface SelectedDomainRepository extends JpaRepository<SelectedDomainEntity, String> {
	@Query(value = "(SELECT * FROM selected_domains WHERE selected_domain_id in :selectedDomainIds AND user_id = :userId) ORDER BY id ASC;", nativeQuery = true)
	List<SelectedDomainEntity> findAllSelectedDomainsBySelectedDomainIdsAndUserId(
			@Param("selectedDomainIds") List<String> selectedDomainIds, @Param("userId") String userId);
}
