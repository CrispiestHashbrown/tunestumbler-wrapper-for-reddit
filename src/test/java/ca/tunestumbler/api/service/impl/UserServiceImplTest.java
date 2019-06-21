package ca.tunestumbler.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.shared.dto.UserDTO;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUser() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId("abc");
		userEntity.setEmail("tester123@tester.com");
		userEntity.setEncryptedPassword("12324");
		userEntity.setRedditAccountName("tester");
		userEntity.setRefreshToken("123");
		userEntity.setLastModified("123");
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDTO userDTO = userService.getUser("testing@testing.com");
		
		assertNotNull(userDTO);
		assertEquals("abc", userDTO.getUserId());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
	}

}
