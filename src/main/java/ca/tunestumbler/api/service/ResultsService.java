package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.NextResultsRequestDTO;
import ca.tunestumbler.api.shared.dto.PlaylistDTO;
import ca.tunestumbler.api.shared.dto.ResultsResponseDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface ResultsService {
	ResultsResponseDTO fetchResults(UserDTO user, String orderBy);

	ResultsResponseDTO fetchNextResults(UserDTO user, NextResultsRequestDTO nextResultsRequestDTO);

	List<PlaylistDTO> fetchYoutubePlaylists(UserDTO user);
}
