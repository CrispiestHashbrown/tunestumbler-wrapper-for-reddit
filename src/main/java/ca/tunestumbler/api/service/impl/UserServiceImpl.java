package ca.tunestumbler.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.UserRepository;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public UserDTO createUser(UserDTO user) {

		if(userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Record already exists");
		}
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		String publicUserId = sharedUtils.generateUserId(50);
		// For testing
		// TODO: Encrypt password and generate public user ID
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword("testUserPassword");

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDTO newUser = new UserDTO();
		BeanUtils.copyProperties(storedUserDetails, newUser);

		return newUser;
	}

}
