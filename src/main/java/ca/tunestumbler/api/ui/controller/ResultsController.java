package ca.tunestumbler.api.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
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
import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.shared.dto.ResultsRequestDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.ResultsRequestModel;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;

@RestController
@RequestMapping("/results")
public class ResultsController {

	@Autowired
	ResultsService resultsService;

	@Autowired
	UserService userService;

	@GetMapping(path = "/fetch/{userId}/{orderBy}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultsResponseModel fetchResults(@PathVariable String userId, @PathVariable String orderBy) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(orderBy)) {
			throw new MissingPathParametersException(ErrorPrefixes.RESULTS_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		ResultsResponseModel resultsResponse = new ResultsResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<ResultsDTO> fetchedResults = resultsService.fetchResults(userDTO, orderBy);
		List<ResultsObjectResponseModel> responseObject = new ArrayList<>();
		Type listType = new TypeToken<List<ResultsObjectResponseModel>>() {
		}.getType();
		responseObject = new ModelMapper().map(fetchedResults, listType);
		resultsResponse.setResults(responseObject);

		return resultsResponse;
	}

	@PostMapping(path = "/fetch/next/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fetchNextResults(@PathVariable String userId,
			@Valid @RequestBody ResultsRequestModel results, BindingResult bindingResult) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.RESULTS_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidBodyException(
					ErrorPrefixes.RESULTS_CONTROLLER.getErrorPrefix() + ErrorMessages.INVALID_BODY.getErrorMessage());
		}

		ResultsResponseModel resultsResponse = new ResultsResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);

		ResultsRequestDTO resultsRequestDTO = new ResultsRequestDTO();
		BeanUtils.copyProperties(results, resultsRequestDTO);

		List<ResultsDTO> updatedResults = resultsService.fetchNextResults(userDTO, resultsRequestDTO.getNextUri(),
				resultsRequestDTO.getAfterId());
		List<ResultsObjectResponseModel> responseObject = new ArrayList<>();
		Type listType = new TypeToken<List<ResultsObjectResponseModel>>() {
		}.getType();
		responseObject = new ModelMapper().map(updatedResults, listType);
		resultsResponse.setResults(responseObject);

		return new ResponseEntity<>(resultsResponse, HttpStatus.CREATED);
	}

}
