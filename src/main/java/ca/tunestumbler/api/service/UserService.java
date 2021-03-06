package ca.tunestumbler.api.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import ca.tunestumbler.api.shared.dto.UserDTO;

public interface UserService extends UserDetailsService {
	UserDTO createUser(UserDTO user);

	UserDTO getUser(String email);

	UserDTO getUserByUserId(String userId);

	UserDTO updateUser(String userId, UserDTO user);
	
	void voidUpdateUser(String userId, UserDTO user);

	void clearUserTokens(String userId);
	
	void deleteUser(String userId);

	List<UserDTO> getUsers(int page, int limit);

}
