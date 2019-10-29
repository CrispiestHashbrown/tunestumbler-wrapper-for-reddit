package ca.tunestumbler.api.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import ca.tunestumbler.api.service.SubredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.service.impl.helpers.AuthorizationHelpers;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;
import ca.tunestumbler.api.ui.model.response.SubredditResponseModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditObjectResponseModel;

@RestController
@RequestMapping("/subreddits")
public class SubredditController {

	@Autowired
	SubredditService subredditService;

	@Autowired
	UserService userService;
	
	@Autowired
	AuthorizationHelpers authorizationHelpers;

	@GetMapping(path = "/fetch/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubredditResponseModel fetchSubreddits(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.SUBREDDIT_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		SubredditResponseModel subredditResponse = new SubredditResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<SubredditDTO> fetchedSubreddits = subredditService.fetchSubreddits(userDTO);
		List<SubredditObjectResponseModel> responseObject = new ArrayList<>();
		Type listType = new TypeToken<List<SubredditObjectResponseModel>>() {
		}.getType();
		responseObject = new ModelMapper().map(fetchedSubreddits, listType);
		subredditResponse.setSubreddits(responseObject);

		return subredditResponse;
	}

	@GetMapping(path = "/update/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubredditResponseModel updateSubreddits(@PathVariable String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			throw new MissingPathParametersException(ErrorPrefixes.SUBREDDIT_CONTROLLER.getErrorPrefix()
					+ ErrorMessages.MISSING_REQUIRED_PATH_FIELD.getErrorMessage());
		}
		
		/*
		 * Token authorization validation
		*/		
		authorizationHelpers.isAuthorized(userId);

		SubredditResponseModel subredditResponse = new SubredditResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<SubredditDTO> updatedSubreddits = subredditService.updateSubreddits(userDTO);
		List<SubredditObjectResponseModel> responseObject = new ArrayList<>();
		Type listType = new TypeToken<List<SubredditObjectResponseModel>>() {
		}.getType();
		responseObject = new ModelMapper().map(updatedSubreddits, listType);
		subredditResponse.setSubreddits(responseObject);

		return subredditResponse;
	}

}
