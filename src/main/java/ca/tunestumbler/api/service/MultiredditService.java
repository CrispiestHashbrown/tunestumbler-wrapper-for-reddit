package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface MultiredditService {
	List<MultiredditDTO> fetchMultireddits(UserDTO user);

	List<MultiredditDTO> updateMultireddits(UserDTO user);

	List<MultiredditDTO> getMultiredditsByUserId(UserDTO user);
}
