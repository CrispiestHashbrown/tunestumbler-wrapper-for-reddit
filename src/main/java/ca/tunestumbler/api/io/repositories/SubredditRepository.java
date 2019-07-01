package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.SubredditEntity;

public interface SubredditRepository extends JpaRepository<SubredditEntity, String> {
	SubredditEntity findBySubredditId(String subredditId);

	SubredditEntity findByUserId(String userId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND subreddit = :subreddit AND id > :maxId", nativeQuery = true)
	SubredditEntity findByUserIdAndSubredditAndMaxId(@Param("userId") String userId,
			@Param("subreddit") String subreddit, @Param("maxId") long maxId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND subreddit = :subreddit AND start_id = :startId", nativeQuery = true)
	SubredditEntity findByUserIdAndSubredditAndMaxStartId(@Param("userId") String userId,
			@Param("subreddit") String subreddit, @Param("startId") long startId);

	@Query(value = "SELECT MAX(id) FROM subreddit", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM subreddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM subreddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND id > :maxId", nativeQuery = true)
	List<SubredditEntity> findSubredditsByUserIdAndMaxId(@Param("userId") String userId, @Param("maxId") long maxId);

	@Query(value = "SELECT * FROM subreddit WHERE user_id = :userId AND start_id = :startId", nativeQuery = true)
	List<SubredditEntity> findSubredditsByUserIdAndMaxStartId(@Param("userId") String userId,
			@Param("startId") long startId);
}
