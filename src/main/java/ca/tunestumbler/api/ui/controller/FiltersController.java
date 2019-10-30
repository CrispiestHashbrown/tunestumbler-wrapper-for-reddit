package ca.tunestumbler.api.ui.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import ca.tunestumbler.api.service.FiltersService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.FiltersRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

@RestController
@RequestMapping("/filters")
public class FiltersController {

	@Autowired
	FiltersService filtersService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FiltersDTO> getFilters(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		UserDTO userDTO = userService.getUserByUserId(userId);
		return filtersService.getFiltersByUserId(userDTO);
	}

	@PostMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createFilters(@PathVariable String userId,
			@Valid @RequestBody FiltersRequestModel newFilters, BindingResult bindingResult) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(
					ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix() + ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
		return new ResponseEntity<>(filtersService.createFilters(userDTO, newFilters.getFilters()), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FiltersDTO> updateFilters(@PathVariable String userId,
			@Valid @RequestBody FiltersRequestModel filtersToUpdate, BindingResult bindingResult) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(
					ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix() + ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
		return filtersService.updateFilters(userDTO, filtersToUpdate.getFilters());
	}

}
