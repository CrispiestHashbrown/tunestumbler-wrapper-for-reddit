package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.exceptions.RecordAlreadyExistsException;
import ca.tunestumbler.api.exceptions.ResourceNotFoundException;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDTO createUser(UserDTO user) {
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RecordAlreadyExistsException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String publicUserId = sharedUtils.generateUserId(50);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setLastModified(sharedUtils.getCurrentTime());

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDTO newUser = new UserDTO();
		BeanUtils.copyProperties(storedUserDetails, newUser);

		return newUser;
	}

	@Override
	public UserDTO getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		UserDTO existingUser = new UserDTO();
		BeanUtils.copyProperties(userEntity, existingUser);
		return existingUser;
	}

	@Override
	public UserDTO getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		UserDTO existingUser = new UserDTO();
		BeanUtils.copyProperties(userEntity, existingUser);
		return existingUser;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDTO updateUser(String userId, UserDTO user) {
		UserDTO userToUpdate = new UserDTO();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		BeanUtils.copyProperties(user, userEntity);
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, userToUpdate);

		return userToUpdate;
	}
	
	@Override
	public void voidUpdateUser(String userId, UserDTO user) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		BeanUtils.copyProperties(user, userEntity);
		userRepository.save(userEntity);
	}

	@Override
	public void clearUserTokens(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		userEntity.setToken(null);
		userEntity.setRefreshToken(null);		
		userRepository.save(userEntity);
	}
	
	@Override
	public void deleteUser(String userId) {
		UserEntity userToDelete = userRepository.findByUserId(userId);

		if (userToDelete == null) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.USER_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		userRepository.delete(userToDelete);
	}

	@Override
	public List<UserDTO> getUsers(int page, int limit) {
		List<UserDTO> existingUsers = new ArrayList<>();

		if (page > 0) {
			page--;
		}

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> usersList = usersPage.getContent();

		for (UserEntity user : usersList) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(user, userDTO);
			existingUsers.add(userDTO);
		}

		return existingUsers;
	}

}
