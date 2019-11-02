package ca.tunestumbler.api.ui.controller;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.MissingPathParametersException;
import ca.tunestumbler.api.service.MultiredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.MultiredditResponseModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditObjectResponseModel;

@RestController
@RequestMapping("/multireddits")
public class MultiredditController {

	@Autowired
	MultiredditService multiredditService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/fetch/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MultiredditResponseModel fetchMultireddits(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.MULTIREDDIT_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		MultiredditResponseModel multiredditResponse = new MultiredditResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<MultiredditDTO> fetchedMultireddits = multiredditService.fetchMultireddits(userDTO);
		Type listType = new TypeToken<List<MultiredditObjectResponseModel>>() {
		}.getType();
		List<MultiredditObjectResponseModel> responseObject = new ModelMapper().map(fetchedMultireddits, listType);
		multiredditResponse.setMultireddits(responseObject);

		return multiredditResponse;
	}

	@GetMapping(path = "/update/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MultiredditResponseModel updateMultireddits(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.SUBREDDIT_SERVICE.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		MultiredditResponseModel multiredditResponse = new MultiredditResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<MultiredditDTO> updatedMultireddits = multiredditService.updateMultireddits(userDTO);
		Type listType = new TypeToken<List<MultiredditObjectResponseModel>>() {
		}.getType();
		List<MultiredditObjectResponseModel> responseObject = new ModelMapper().map(updatedMultireddits, listType);
		multiredditResponse.setMultireddits(responseObject);

		return multiredditResponse;
	}

}
