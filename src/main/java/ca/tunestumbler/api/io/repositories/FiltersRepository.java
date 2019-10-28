package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.FiltersEntity;

public interface FiltersRepository extends JpaRepository<FiltersEntity, String> {
	@Query(value = "SELECT * FROM filters WHERE user_id = :userId AND is_active = true", nativeQuery = true)
	List<FiltersEntity> findFiltersByUserIdAndIsActive(@Param("userId") String userId);
}
