package ca.tunestumbler.api.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.exceptions.ResourceNotFoundException;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.auth.AuthConnectResponseModel;

@RestController
@RequestMapping("/auth")
public class AuthValidationController {

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	AuthValidationService authValidationService;

	@Autowired
	UserService userService;

	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/connect", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> connectMyRedditAccount() {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		/*
		 * Token authorization validation
		 */
		authorizationHelpers.isAuthorized(userId);

		if (userService.getUserByUserId(userId) == null) {
			throw new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		AuthValidationDTO authValidationDTO = authValidationService.createAuthState(userId);

		AuthConnectResponseModel authConnectResponseModel = new AuthConnectResponseModel();
		BeanUtils.copyProperties(authValidationDTO, authConnectResponseModel);

		return new ResponseEntity<>(authConnectResponseModel, HttpStatus.CREATED);
	}

	@GetMapping(path = "/connect/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> connectRedditAccount(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		/*
		 * Token authorization validation
		 */
		authorizationHelpers.isAuthorized(userId);

		if (userService.getUserByUserId(userId) == null) {
			throw new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		AuthValidationDTO authValidationDTO = authValidationService.createAuthState(userId);

		AuthConnectResponseModel authConnectResponseModel = new AuthConnectResponseModel();
		BeanUtils.copyProperties(authValidationDTO, authConnectResponseModel);

		return new ResponseEntity<>(authConnectResponseModel, HttpStatus.CREATED);
	}

	@GetMapping(path = "/handler", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> validateStateAndRedirect(@RequestParam String state, @RequestParam String code) {
		if (Strings.isNullOrEmpty(state) || Strings.isNullOrEmpty(code)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		HttpHeaders authHeaders = authValidationService.createHandlerHeaders(state, code);
		if (!authHeaders.isEmpty()) {
			return new ResponseEntity<>(authHeaders, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/refresh_token", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> refreshMyToken() {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		/*
		 * Token authorization validation
		 */
		authorizationHelpers.isAuthorized(userId);

		HttpHeaders authHeaders = authValidationService.createRefreshTokenHeaders(userId);
		if (!authHeaders.isEmpty()) {
			return new ResponseEntity<>(authHeaders, HttpStatus.OK);			
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/refresh_token/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> refreshToken(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AUTH_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		/*
		 * Token authorization validation
		 */
		authorizationHelpers.isAuthorized(userId);

		HttpHeaders authHeaders = authValidationService.createRefreshTokenHeaders(userId);
		if (!authHeaders.isEmpty()) {
			return new ResponseEntity<>(authHeaders, HttpStatus.OK);			
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
