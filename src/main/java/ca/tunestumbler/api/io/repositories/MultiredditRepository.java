package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.MultiredditEntity;

public interface MultiredditRepository extends JpaRepository<MultiredditEntity, String> {
	@Query(value = "SELECT MAX(id) FROM multireddit", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM multireddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM multireddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND start_id >= :startId AND is_curated = true", nativeQuery = true)
	List<MultiredditEntity> findSubredditsByUserIdAndMaxStartIdAndCurated(@Param("userId") String userId,
			@Param("startId") Long startId);
}
