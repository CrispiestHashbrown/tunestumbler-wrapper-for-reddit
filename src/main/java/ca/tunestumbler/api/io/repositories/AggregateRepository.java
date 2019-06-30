package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.AggregateEntity;

public interface AggregateRepository extends CrudRepository<AggregateEntity, String> {
	AggregateEntity findByAggregateId(String aggregateId);

	AggregateEntity findByUserId(String userId);

	@Query(value = "SELECT MAX(id) FROM aggregate", nativeQuery = true)
	Long getMaxId();

	@Query(value = "SELECT MAX(id) FROM aggregate WHERE user_id = :userId", nativeQuery = true)
	Long getMaxIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT MAX(start_id) FROM aggregate WHERE user_id = :userId", nativeQuery = true)
	Long getMaxStartIdByUserId(@Param("userId") String userId);

	@Query(value = "SELECT * FROM aggregate WHERE user_id = :userId AND id > :maxId", nativeQuery = true)
	List<AggregateEntity> findAggregateByUserIdAndMaxId(@Param("userId") String userId, @Param("maxId") long maxId);
}
