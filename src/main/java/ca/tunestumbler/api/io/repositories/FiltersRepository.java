package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.FiltersEntity;

public interface FiltersRepository extends JpaRepository<FiltersEntity, String> {
	FiltersEntity findByFiltersId(String filtersId);

	@Query(value = "SELECT * FROM filters WHERE user_id = :userId", nativeQuery = true)
	List<FiltersEntity> findFiltersByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(id) FROM filters", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM filters WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM filters WHERE user_id = :userId", nativeQuery = true)
	Long findMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM filters WHERE user_id = :userId AND start_id >= :startId AND is_active = true", nativeQuery = true)
	List<FiltersEntity> findFiltersByUserIdAndStartIdAndIsActive(@Param("userId") String userId,
			@Param("startId") long startId);
}
