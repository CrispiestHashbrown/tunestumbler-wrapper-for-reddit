package ca.tunestumbler.api.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.tunestumbler.api.exceptions.UserServiceException;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.UserDetailsRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.UserDetailsResponseModel;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserDetailsResponseModel getUser(@PathVariable String userId) {
		UserDetailsResponseModel user = new UserDetailsResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDTO, user);

		return user;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserDetailsResponseModel newUser = new UserDetailsResponseModel();

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO createdUser = userService.createUser(userDTO);
		BeanUtils.copyProperties(createdUser, newUser);

		return newUser;
	}

	@PutMapping(path = "/{userId}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserDetailsResponseModel updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
		UserDetailsResponseModel user = new UserDetailsResponseModel();

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		BeanUtils.copyProperties(updatedUser, user);
		
		return user;
	}

	@DeleteMapping
	public String deleteUser() {
		return "delete user was called";
	}

}
