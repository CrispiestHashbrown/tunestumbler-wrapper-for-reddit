package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.SubredditEntity;

public interface SubredditRepository extends CrudRepository<SubredditEntity, String> {
	SubredditEntity findBySubredditId(String subredditId);

	SubredditEntity findByUserId(String userId);

	@Query(value = "SELECT MAX(id) FROM subreddit", nativeQuery = true)
	Long getMaxId();

	@Query(value = "SELECT MAX(id) FROM subreddit WHERE user_id = :userId", nativeQuery = true)
	Long getMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM subreddit WHERE user_id = :userId", nativeQuery = true)
	Long getMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND id > :maxId", nativeQuery = true)
	List<SubredditEntity> findSubredditsByUserIdAndMaxId(@Param("userId") String userId, @Param("maxId") long maxId);
}
