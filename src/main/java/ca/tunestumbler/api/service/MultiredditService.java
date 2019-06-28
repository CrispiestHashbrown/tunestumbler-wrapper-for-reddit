package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface MultiredditService {
	List<MultiredditDTO> fetchAndUpdateMultireddits(UserDTO user);

	MultiredditDTO getMultiredditsById(String multiredditId);

	List<MultiredditDTO> getMultiredditsByUserId(String userId);

	List<MultiredditDTO> getMultiredditsByUserIdAndId(String userId, String id);

	void deleteMultireddits(String userId);
}
