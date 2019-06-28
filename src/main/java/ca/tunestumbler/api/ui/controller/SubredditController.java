package ca.tunestumbler.api.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.tunestumbler.api.service.SubredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.SubredditResponseModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditObjectResponseModel;

@RestController
@RequestMapping("/subreddits")
public class SubredditController {

	@Autowired
	SubredditService subredditService;

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubredditResponseModel fetchAndUpdateSubreddits(@PathVariable String userId) {
		SubredditResponseModel subredditResponse = new SubredditResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<SubredditDTO> fetchedSubreddits = subredditService.fetchAndUpdateSubreddits(userDTO);
		List<SubredditObjectResponseModel> responseObject = new ArrayList<>();
		Type listType = new TypeToken<List<SubredditObjectResponseModel>>() {
		}.getType();
		responseObject = new ModelMapper().map(fetchedSubreddits, listType);
		subredditResponse.setSubreddits(responseObject);

		return subredditResponse;
	}

}
