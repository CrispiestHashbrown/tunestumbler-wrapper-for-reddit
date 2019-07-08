package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface ResultsService {
	List<ResultsDTO> fetchResults(UserDTO user, String orderBy);

	List<ResultsDTO> fetchNextResults(UserDTO user, String nextUri, String nextAfterId);
}
