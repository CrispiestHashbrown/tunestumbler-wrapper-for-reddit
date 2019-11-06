package ca.tunestumbler.api.ui.controller;

import java.lang.reflect.Type;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import ca.tunestumbler.api.ui.model.response.FiltersResponseModel;
import ca.tunestumbler.api.ui.model.response.filters.FiltersObjectResponseModel;

@RestController
@RequestMapping("/filters")
public class FiltersController {

	@Autowired
	FiltersService filtersService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/myfilters", produces = MediaType.APPLICATION_JSON_VALUE)
	public FiltersResponseModel getMyFilters() {
		String userId = authorizationHelpers.getUserIdFromAuth();
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		FiltersResponseModel filtersResponse = new FiltersResponseModel();
		
		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> existingFilters = filtersService.getFiltersByUserId(userDTO);
		Type listType = new TypeToken<List<FiltersObjectResponseModel>>() {	
		}.getType();	
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(existingFilters, listType);	
		filtersResponse.setFilters(responseObject);	

		return filtersResponse;
	}

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public FiltersResponseModel getFilters(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		FiltersResponseModel filtersResponse = new FiltersResponseModel();
		
		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> existingFilters = filtersService.getFiltersByUserId(userDTO);
		Type listType = new TypeToken<List<FiltersObjectResponseModel>>() {	
		}.getType();	
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(existingFilters, listType);	
		filtersResponse.setFilters(responseObject);	

		return filtersResponse;
	}

	@PostMapping(path = "/myfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createMyFilters(@Valid @RequestBody FiltersRequestModel newFilters,
			BindingResult bindingResult) {
		String userId = authorizationHelpers.getUserIdFromAuth();
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
		
		if (newFilters.getFilters().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> createdFilters = filtersService.createFilters(userDTO, newFilters.getFilters());

		Type responseListType = new TypeToken<List<FiltersObjectResponseModel>>() {
		}.getType();
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(createdFilters, responseListType);
		FiltersResponseModel newFiltersResponse = new FiltersResponseModel();
		newFiltersResponse.setFilters(responseObject);

		return new ResponseEntity<>(newFiltersResponse, HttpStatus.CREATED);
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
		
		if (newFilters.getFilters().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> createdFilters = filtersService.createFilters(userDTO, newFilters.getFilters());

		Type responseListType = new TypeToken<List<FiltersObjectResponseModel>>() {
		}.getType();
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(createdFilters, responseListType);
		FiltersResponseModel newFiltersResponse = new FiltersResponseModel();
		newFiltersResponse.setFilters(responseObject);

		return new ResponseEntity<>(newFiltersResponse, HttpStatus.CREATED);
	}

	@PutMapping(path = "/myfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMyFilters(@Valid @RequestBody FiltersRequestModel filtersToUpdate,
			BindingResult bindingResult) {
		String userId = authorizationHelpers.getUserIdFromAuth();
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
		
		if (filtersToUpdate.getFilters().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> updatedFilters = filtersService.updateFilters(userDTO, filtersToUpdate.getFilters());	

		Type responseListType = new TypeToken<List<FiltersObjectResponseModel>>() {	
		}.getType();	
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(updatedFilters, responseListType);	
		FiltersResponseModel updatedFiltersResponse = new FiltersResponseModel();
		updatedFiltersResponse.setFilters(responseObject);	

		return new ResponseEntity<>(updatedFiltersResponse, HttpStatus.OK);
	}

	@PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateFilters(@PathVariable String userId,
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
		
		if (filtersToUpdate.getFilters().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> updatedFilters = filtersService.updateFilters(userDTO, filtersToUpdate.getFilters());	

		Type responseListType = new TypeToken<List<FiltersObjectResponseModel>>() {	
		}.getType();	
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(updatedFilters, responseListType);	
		FiltersResponseModel updatedFiltersResponse = new FiltersResponseModel();
		updatedFiltersResponse.setFilters(responseObject);	

		return new ResponseEntity<>(updatedFiltersResponse, HttpStatus.OK);
	}

}
