package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface SubredditService {
	List<SubredditDTO> fetchAndUpdateSubreddits(UserDTO user);

	List<SubredditDTO> getSubredditsById(String subredditId);

	List<SubredditDTO> getSubredditsByUserId(String userId);

	List<SubredditDTO> getSubredditsByUserIdAndId(String userId, String id);

	void deleteSubreddits(String userId);
}
