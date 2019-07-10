package ca.tunestumbler.api.io.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.ResultsEntity;

public interface ResultsRepository extends JpaRepository<ResultsEntity, String>, JpaSpecificationExecutor<ResultsEntity> {
	ResultsEntity findByResultsId(String resultsId);

	@Query(value = "SELECT MAX(id) FROM results", nativeQuery = true)
	Long findMaxId();

	@Query(value = "SELECT MAX(id) FROM results WHERE user_id = :userId", nativeQuery = true)
	Long findMaxIdByUserId(@Param("userId") String userId);
}
