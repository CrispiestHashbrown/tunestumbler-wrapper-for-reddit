package ca.tunestumbler.api.io.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.FiltersEntity;

public interface FiltersRepository extends JpaRepository<FiltersEntity, String> {
	@Query(value = "(SELECT * FROM filters WHERE user_id = :userId) ORDER BY id ASC;", nativeQuery = true)
	List<FiltersEntity> findFiltersByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM filters WHERE filters_id in :filters AND user_id = :userId", nativeQuery = true)
	List<FiltersEntity> findFiltersByFilterIdListAndUserId(@Param("filters") List<String> filters,
			@Param("userId") String userId);

	@Query(value = "(SELECT subreddit FROM filters WHERE user_id = :userId) ORDER BY id ASC;", nativeQuery = true)
	Set<String> findSubredditsByUserId(@Param("userId") String userId);
}
