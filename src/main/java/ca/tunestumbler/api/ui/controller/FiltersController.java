package ca.tunestumbler.api.ui.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.InvalidBodyException;
import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.service.FiltersService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.mapper.FilterMapper;
import ca.tunestumbler.api.ui.model.request.filter.create.CreateFiltersRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.delete.IdsToDeleteRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.UpdateFiltersRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.filter.FiltersResponseModel;

@RestController
@RequestMapping("/filters")
public class FiltersController {

	@Autowired
	FiltersService filtersService;

	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@Autowired
	FilterMapper filterMapper;

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

		List<FiltersDTO> existingFilters = filtersService.getFiltersByUserId(userId);
		FiltersResponseModel filtersResponse = new FiltersResponseModel();
		filtersResponse.setFilters(filterMapper.filtersDTOlistToResponseObjects(existingFilters));

		return filtersResponse;
	}

	@PostMapping(path = "/myfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createMyFilters(@Valid @RequestBody CreateFiltersRequestModel newFilters,
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

		List<FiltersDTO> createdFilters = filtersService.createFilters(userId,
				filterMapper.createRequestObjectsToFiltersDTOs(newFilters.getFilters()));
		FiltersResponseModel newFiltersResponse = new FiltersResponseModel();
		newFiltersResponse.setFilters(filterMapper.filtersDTOlistToResponseObjects(createdFilters));

		return new ResponseEntity<>(newFiltersResponse, HttpStatus.CREATED);
	}

	@PutMapping(path = "/myfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateMyFilters(@Valid @RequestBody UpdateFiltersRequestModel filtersToUpdate,
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

		List<FiltersDTO> updatedFilters = filtersService.updateFilters(userId,
				filterMapper.updateRequestObjectsToFiltersDTOs(filtersToUpdate.getFilters()));
		FiltersResponseModel updatedFiltersResponse = new FiltersResponseModel();
		updatedFiltersResponse.setFilters(filterMapper.filtersDTOlistToResponseObjects(updatedFilters));

		return new ResponseEntity<>(updatedFiltersResponse, HttpStatus.OK);
	}

	@DeleteMapping(path = "/myfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteFilters(@Valid @RequestBody IdsToDeleteRequestModel filtersToDelete,
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

		filtersService.deleteFilters(userId, filtersToDelete.getIdsToDelete());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/myexcludeddomainfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteExcludedDomainFilters(@Valid @RequestBody IdsToDeleteRequestModel excludedDomains,
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

		filtersService.deleteExcludedDomains(userId, excludedDomains.getIdsToDelete());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/myexcludedkeywordfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteExcludedKeywordFilters(@Valid @RequestBody IdsToDeleteRequestModel excludedKeywords,
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

		filtersService.deleteExcludedKeywords(userId, excludedKeywords.getIdsToDelete());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/myselecteddomainfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteSelectedDomainFilters(@Valid @RequestBody IdsToDeleteRequestModel selectedDomains,
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

		filtersService.deleteSelectedDomains(userId, selectedDomains.getIdsToDelete());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/myselectedkeywordfilters", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteSelectedKeywordFilters(@Valid @RequestBody IdsToDeleteRequestModel selectedKeywords,
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

		filtersService.deleteSelectedKeywords(userId, selectedKeywords.getIdsToDelete());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
