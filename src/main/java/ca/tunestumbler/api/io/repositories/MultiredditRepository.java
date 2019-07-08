package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.MultiredditEntity;

public interface MultiredditRepository extends JpaRepository<MultiredditEntity, String> {
	MultiredditEntity findByMultiredditId(String multiredditId);

	MultiredditEntity findByUserId(String userId);

//	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND multireddit = :multireddit AND id > :maxId", nativeQuery = true)
//	List<MultiredditEntity> findByUserIdAndMultiredditAndMaxId(@Param("userId") String userId,
//			@Param("multireddit") String multireddit, @Param("maxId") long maxId);

//	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND subreddit = :subreddit AND id > :maxId", nativeQuery = true)
//	MultiredditEntity findByUserIdAndSubredditAndMaxId(@Param("userId") String userId,
//			@Param("subreddit") String subreddit, @Param("maxId") long maxId);

	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND multireddit = :multireddit AND start_id >= :startId", nativeQuery = true)
	List<MultiredditEntity> findByUserIdAndMultiredditAndMaxStartId(@Param("userId") String userId,
			@Param("multireddit") String multireddit, @Param("startId") long startId);

	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND subreddit = :subreddit AND start_id >= :startId", nativeQuery = true)
	MultiredditEntity findByUserIdAndSubredditAndMaxStartId(@Param("userId") String userId,
			@Param("subreddit") String subreddit, @Param("startId") long startId);

	@Query(value = "SELECT MAX(id) FROM multireddit", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM multireddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM multireddit WHERE user_id = :userId", nativeQuery = true)
	Long findMaxStartIdByUserId(@Param("userId") String userId);
//
//	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND id > :maxId AND is_curated = true", nativeQuery = true)
//	List<MultiredditEntity> findSubredditsByUserIdAndMaxIdAndCurated(@Param("userId") String userId,
//			@Param("maxId") long maxId);

	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND start_id >= :startId AND is_curated = true", nativeQuery = true)
	List<MultiredditEntity> findSubredditsByUserIdAndMaxStartIdAndCurated(@Param("userId") String userId,
			@Param("startId") long startId);
}
