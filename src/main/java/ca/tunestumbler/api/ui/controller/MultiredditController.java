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

import ca.tunestumbler.api.service.MultiredditService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.MultiredditResponseModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditObjectResponseModel;

@RestController
@RequestMapping("/multireddits")
public class MultiredditController {

	@Autowired
	MultiredditService multiredditService;

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public MultiredditResponseModel fetchAndUpdateMultireddits(@PathVariable String userId) {
		MultiredditResponseModel multiredditResponse = new MultiredditResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<MultiredditDTO> fetchedMultireddits = multiredditService.fetchAndUpdateMultireddits(userDTO);
		List<MultiredditObjectResponseModel> responseObject = new ArrayList<>();
		Type listType = new TypeToken<List<MultiredditObjectResponseModel>>() {
		}.getType();
		responseObject = new ModelMapper().map(fetchedMultireddits, listType);
		multiredditResponse.setMultireddits(responseObject);

		return multiredditResponse;
	}
}
