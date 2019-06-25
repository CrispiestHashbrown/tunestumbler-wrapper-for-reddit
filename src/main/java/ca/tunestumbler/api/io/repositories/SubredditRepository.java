package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.SubredditEntity;

public interface SubredditRepository {
	SubredditEntity findBySubredditId(String subredditId);

	SubredditEntity findByUserId(String userId);

	@Query(value = "SELECT MAX(sub.id) FROM subreddit sub WHERE sub.userId = :userId", nativeQuery = true)
	long getMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM subreddit sub WHERE userId = :userId AND sub.id > :maxId", nativeQuery = true)
	List<SubredditEntity> findSubredditsByUserIdAndGreaterThanMaxId(@Param("userId") String userId,
			@Param("maxId") long maxId);
}
