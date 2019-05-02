package ca.tunestumbler.api.service;

import ca.tunestumbler.api.shared.dto.UserDTO;

public interface UserService {
	UserDTO createUser(UserDTO user);
}
