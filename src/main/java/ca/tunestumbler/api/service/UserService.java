package ca.tunestumbler.api.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ca.tunestumbler.api.shared.dto.UserDTO;

public interface UserService extends UserDetailsService {
	UserDTO createUser(UserDTO user);

	UserDTO getUser(String email);

	UserDTO getUserByUserId(String userId);
}
