package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.FiltersDTO;

public interface FiltersService {
	List<FiltersDTO> createFilters(String userId, List<FiltersDTO> filters);

	List<FiltersDTO> getFiltersByUserId(String userId);

	List<FiltersDTO> updateFilters(String userId, List<FiltersDTO> filters);

	void deleteFilters(String userId, List<String> filters);

	void deleteExcludedDomains(String userId, List<String> excludedDomains);

	void deleteExcludedKeywords(String userId, List<String> excludedKeywords);

	void deleteSelectedDomains(String userId, List<String> selectedDomains);

	void deleteSelectedKeywords(String userId, List<String> selectedKeywords);
}
