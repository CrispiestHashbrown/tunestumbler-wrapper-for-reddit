package ca.tunestumbler.api.ui.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.InvalidBodyException;
import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.UserDetailsRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.UserDetailsResponseModel;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/myprofile", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetailsResponseModel getMyProfile() {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDetailsResponseModel existingUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDTO, existingUserResponse);

		return existingUserResponse;
	}

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetailsResponseModel getUser(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDetailsResponseModel existingUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDTO, existingUserResponse);

		return existingUserResponse;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDetailsResponseModel newUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO createdUser = userService.createUser(userDTO);
		BeanUtils.copyProperties(createdUser, newUserResponse);

		return new ResponseEntity<>(newUserResponse, HttpStatus.CREATED);
	}

	@PutMapping(path = "/myprofile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetailsResponseModel updateMyProfile(@Valid @RequestBody UserDetailsRequestModel userDetails,
			BindingResult bindingResult) {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDetailsResponseModel updatedUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		BeanUtils.copyProperties(updatedUser, updatedUserResponse);

		return updatedUserResponse;
	}

	@PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetailsResponseModel updateUser(@PathVariable String userId,
			@Valid @RequestBody UserDetailsRequestModel userDetails, BindingResult bindingResult) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDetailsResponseModel updatedUserResponse = new UserDetailsResponseModel();

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		BeanUtils.copyProperties(updatedUser, updatedUserResponse);

		return updatedUserResponse;
	}

	@GetMapping(path = "/myprofile/clear_tokens", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clearMyTokens() {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		userService.clearUserTokens(userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/{userId}/clear_tokens", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clearTokens(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		userService.clearUserTokens(userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping(path = "/myprofile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteMyProfile() {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		userService.deleteUser(userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUser(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.USER_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		userService.deleteUser(userId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
