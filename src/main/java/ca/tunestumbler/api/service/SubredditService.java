package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface SubredditService {
	List<SubredditDTO> fetchSubreddits(UserDTO user);

	List<SubredditDTO> updateSubreddits(UserDTO user);

	List<SubredditDTO> getSubredditsByUserId(String userId);
}
