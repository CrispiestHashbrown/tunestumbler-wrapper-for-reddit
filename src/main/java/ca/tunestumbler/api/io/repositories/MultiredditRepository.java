package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.MultiredditEntity;

public interface MultiredditRepository {
	MultiredditEntity findByMultiredditId(String multiredditId);

	@Query(value = "SELECT MAX(multi.id) FROM multireddit multi WHERE multi.userId = :userId", nativeQuery = true)
	long getMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM multireddit multi WHERE userId = :userId AND multi.id > :maxId", nativeQuery = true)
	List<MultiredditEntity> findSubredditsByUserIdAndGreaterThanMaxId(@Param("userId") String userId,
			@Param("maxId") long maxId);
}
