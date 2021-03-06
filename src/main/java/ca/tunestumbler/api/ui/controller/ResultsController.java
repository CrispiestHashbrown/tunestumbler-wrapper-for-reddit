package ca.tunestumbler.api.ui.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.InvalidBodyException;
import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.service.ResultsService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.ResultsRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;

@RestController
@RequestMapping("/results")
public class ResultsController {

	@Autowired
	ResultsService resultsService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/fetch/myresults/{orderBy}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultsResponseModel fetchMyResults(@PathVariable String orderBy) {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(orderBy)) {
			throw new MissingPathParametersException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDTO userDTO = userService.getUserByUserId(userId);
		return resultsService.fetchResults(userDTO, orderBy);
	}

	@GetMapping(path = "/fetch/{userId}/{orderBy}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultsResponseModel fetchResults(@PathVariable String userId, @PathVariable String orderBy) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(orderBy)) {
			throw new MissingPathParametersException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDTO userDTO = userService.getUserByUserId(userId);
		return resultsService.fetchResults(userDTO, orderBy);
	}

	@PostMapping(path = "/fetch/next", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fetchMyNextResults(@Valid @RequestBody ResultsRequestModel results,
			BindingResult bindingResult) {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix() 
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDTO userDTO = userService.getUserByUserId(userId);

		ResultsResponseModel resultsResponse = 
				resultsService.fetchNextResults(userDTO, results.getNextUri(), results.getAfterId());

		return new ResponseEntity<>(resultsResponse, HttpStatus.CREATED);
	}

	@PostMapping(path = "/fetch/next/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fetchNextResults(@PathVariable String userId,
			@Valid @RequestBody ResultsRequestModel results, BindingResult bindingResult) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(ErrorPrefixes.RESULTS_SERVICE.getErrorPrefix() 
					+ ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDTO userDTO = userService.getUserByUserId(userId);

		ResultsResponseModel resultsResponse = 
				resultsService.fetchNextResults(userDTO, results.getNextUri(), results.getAfterId());

		return new ResponseEntity<>(resultsResponse, HttpStatus.CREATED);
	}

}
