package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.SubredditEntity;

public interface SubredditRepository extends JpaRepository<SubredditEntity, String> {
	@Query(value = "SELECT MAX(id) FROM subreddit", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM subreddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM subreddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND id > :maxId AND is_subscribed = true", nativeQuery = true)
	List<SubredditEntity> findSubredditsByUserIdAndMaxIdAndSubscribed(@Param("userId") String userId,
			@Param("maxId") Long maxId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND start_id >= :startId AND is_subscribed = true", nativeQuery = true)
	List<SubredditEntity> findSubredditsByUserIdAndMaxStartIdAndSubscribed(@Param("userId") String userId,
			@Param("startId") Long startId);
}
