package ca.tunestumbler.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.UserDTO;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	SharedUtils sharedUtils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	String userId = "abc";
	String encryptedPassword = "12324";
	String randomDate = "July 1, 2020 00:00:00";
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		userEntity = new UserEntity();
		userEntity.setUserId(userId);
		userEntity.setEmail("tester123@tester.com");
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setRedditAccountName("tester");
		userEntity.setRefreshToken("123");
		userEntity.setLastModified("123");
	}

	@Test
	void testGetUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDTO userDTO = userService.getUser("testing@testing.com");

		assertNotNull(userDTO);
		assertEquals(userEntity.getUserId(), userDTO.getUserId());
	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
	}

	@Test
	final void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(sharedUtils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(sharedUtils.getCurrentTime()).thenReturn(randomDate);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

		UserDTO userDTO = new UserDTO();
		UserDTO storedUserDTO = userService.createUser(userDTO);

		assertNotNull(storedUserDTO);
		assertEquals(userEntity.getUserId(), storedUserDTO.getUserId());
	}

}
