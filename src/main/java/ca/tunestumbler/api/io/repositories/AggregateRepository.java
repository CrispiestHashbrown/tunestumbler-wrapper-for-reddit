package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.AggregateEntity;

public interface AggregateRepository extends JpaRepository<AggregateEntity, String> {
	AggregateEntity findByAggregateId(String aggregateId);

	AggregateEntity findByUserId(String userId);

	@Query(value = "SELECT * FROM aggregate WHERE user_id = :userId AND multireddit = :multireddit AND subreddit = :subreddit AND start_id >= :startId", nativeQuery = true)
	List<AggregateEntity> findByUserIdAndMultiredditAndSubredditAndMaxStartId(@Param("userId") String userId,
			@Param("multireddit") String multireddit, @Param("subreddit") String subreddit,
			@Param("startId") Long startId);

	@Query(value = "SELECT * FROM aggregate WHERE user_id = :userId AND subreddit = :subreddit AND start_id >= :startId", nativeQuery = true)
	List<AggregateEntity> findByUserIdAndSubredditAndMaxStartId(@Param("userId") String userId,
			@Param("subreddit") String subreddit, @Param("startId") Long startId);

	@Query(value = "SELECT MAX(id) FROM aggregate", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM aggregate WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM aggregate WHERE user_id = :userId", nativeQuery = true)
	Long findMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM aggregate WHERE user_id = :userId AND start_id >= :startId", nativeQuery = true)
	List<AggregateEntity> findAggregateByUserIdAndMaxStartId(@Param("userId") String userId,
			@Param("startId") Long startId);

	@Query(value = "SELECT * FROM aggregate WHERE user_id = :userId AND start_id >= :startId AND is_subreddit_added = true", nativeQuery = true)
	List<AggregateEntity> findAggregateByUserIdAndMaxStartIdAndIsSubredditAdded(@Param("userId") String userId,
			@Param("startId") Long startId);
}
