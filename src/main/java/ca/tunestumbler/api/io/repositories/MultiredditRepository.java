package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.MultiredditEntity;

public interface MultiredditRepository extends CrudRepository<MultiredditEntity, String> {
	MultiredditEntity findByMultiredditId(String multiredditId);

	MultiredditEntity findByUserId(String userId);

	@Query(value = "SELECT MAX(id) FROM multireddit", nativeQuery = true)
	Long getMaxId();

	@Query(value = "SELECT MAX(id) FROM multireddit WHERE user_id = :userId", nativeQuery = true)
	Long getMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM multireddit WHERE user_id = :userId AND id > :maxId", nativeQuery = true)
	List<MultiredditEntity> findSubredditsByUserIdAndGreaterThanMaxId(@Param("userId") String userId,
			@Param("maxId") long maxId);
}
