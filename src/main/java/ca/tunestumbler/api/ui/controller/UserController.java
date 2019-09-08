package ca.tunestumbler.api.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.InvalidBodyException;
import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.UserDetailsRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.UserDetailsResponseModel;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetailsResponseModel getUser(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		UserDetailsResponseModel existingUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDTO, existingUserResponse);

		return existingUserResponse;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.USER_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDetailsResponseModel newUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO createdUser = userService.createUser(userDTO);
		BeanUtils.copyProperties(createdUser, newUserResponse);

		return new ResponseEntity<>(newUserResponse, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetailsResponseModel updateUser(@PathVariable String userId,
			@Valid @RequestBody UserDetailsRequestModel userDetails, BindingResult bindingResult) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.USER_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDetailsResponseModel updatedUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		BeanUtils.copyProperties(updatedUser, updatedUserResponse);

		return updatedUserResponse;
	}

	@DeleteMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUser(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		userService.deleteUser(userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserDetailsResponseModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "30") int limit) {
		List<UserDetailsResponseModel> existingUsers = new ArrayList<>();

		List<UserDTO> usersDTO = userService.getUsers(page, limit);

		for (UserDTO userDTO : usersDTO) {
			UserDetailsResponseModel existingUser = new UserDetailsResponseModel();
			BeanUtils.copyProperties(userDTO, existingUser);
			existingUsers.add(existingUser);
		}

		return existingUsers;
	}

}
