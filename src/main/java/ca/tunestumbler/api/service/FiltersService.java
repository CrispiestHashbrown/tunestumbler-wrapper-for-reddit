package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface FiltersService {
	List<FiltersDTO> createFilters(UserDTO user, List<FiltersDTO> filters);

	List<FiltersDTO> getFiltersByUserId(UserDTO user);

	List<FiltersDTO> updateFilters(UserDTO user, List<FiltersDTO> filters);

	void deleteFilters(String userId, List<String> filters);
}
