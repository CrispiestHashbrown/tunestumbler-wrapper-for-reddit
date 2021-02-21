package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.ExcludedKeywordEntity;

public interface ExcludedKeywordRepository extends JpaRepository<ExcludedKeywordEntity, String> {
	@Query(value = "(SELECT * FROM excluded_keywords WHERE excluded_keyword_id in :excludedKeywordIds AND user_id = :userId) ORDER BY id ASC;", nativeQuery = true)
	List<ExcludedKeywordEntity> findAllExcludedKeywordsByExcludedKeywordIdsAndUserId(
			@Param("excludedKeywordIds") List<String> excludedKeywordIds, @Param("userId") String userId);
}
