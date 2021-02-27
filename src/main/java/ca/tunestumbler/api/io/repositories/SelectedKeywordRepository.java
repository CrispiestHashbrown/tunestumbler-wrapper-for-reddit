package ca.tunestumbler.api.io.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.tunestumbler.api.io.entity.SelectedKeywordEntity;

public interface SelectedKeywordRepository extends JpaRepository<SelectedKeywordEntity, String> {
	@Query(value = "(SELECT * FROM selected_keywords WHERE selected_keyword_id in :selectedKeywordIds AND user_id = :userId) ORDER BY id ASC;", nativeQuery = true)
	List<SelectedKeywordEntity> findAllSelectedKeywordsBySelectedKeywordIdsAndUserId(
			@Param("selectedKeywordIds") List<String> selectedKeywordIds, @Param("userId") String userId);
}
