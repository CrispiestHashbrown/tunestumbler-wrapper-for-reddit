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
import ca.tunestumbler.api.service.AggregateService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.AggregateDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.AggregateResponseModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.aggregate.AggregateObjectResponseModel;

@RestController
@RequestMapping("/aggregate")
public class AggregateController {

	@Autowired
	AggregateService aggregateService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/create/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AggregateResponseModel createAggregate(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AGGREGATE_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		AggregateResponseModel aggregateResponse = new AggregateResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<AggregateDTO> createdAggregate = aggregateService.createAggregateByUserId(userDTO);
		Type listType = new TypeToken<List<AggregateObjectResponseModel>>() {
		}.getType();
		List<AggregateObjectResponseModel> responseObject = new ModelMapper().map(createdAggregate, listType);
		aggregateResponse.setAggregate(responseObject);

		return aggregateResponse;
	}

	@GetMapping(path = "/update/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AggregateResponseModel updateAggregate(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AGGREGATE_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		AggregateResponseModel updatedAggregateResponse = new AggregateResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<AggregateDTO> updatedAggregate = aggregateService.updateAggregateByUserId(userDTO);
		Type listType = new TypeToken<List<AggregateObjectResponseModel>>() {
		}.getType();
		List<AggregateObjectResponseModel> responseObject = new ModelMapper().map(updatedAggregate, listType);
		updatedAggregateResponse.setAggregate(responseObject);

		return updatedAggregateResponse;
	}

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AggregateResponseModel getAggregate(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.AGGREGATE_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		AggregateResponseModel aggregateResponse = new AggregateResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<AggregateDTO> existingAggregate = aggregateService.getAggregateByUserId(userDTO);
		Type listType = new TypeToken<List<AggregateObjectResponseModel>>() {
		}.getType();
		List<AggregateObjectResponseModel> responseObject = new ModelMapper().map(existingAggregate, listType);
		aggregateResponse.setAggregate(responseObject);

		return aggregateResponse;
	}

}
