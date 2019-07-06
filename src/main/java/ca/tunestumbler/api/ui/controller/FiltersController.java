package ca.tunestumbler.api.ui.controller;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.tunestumbler.api.service.FiltersService;
import ca.tunestumbler.api.service.UserService;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.request.FiltersRequestModel;
import ca.tunestumbler.api.ui.model.response.FiltersResponseModel;
import ca.tunestumbler.api.ui.model.response.filters.FiltersObjectResponseModel;

@RestController
@RequestMapping("/filters")
public class FiltersController {

	@Autowired
	FiltersService filtersService;

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public FiltersResponseModel getFilters(@PathVariable String userId) {
		FiltersResponseModel filtersResponse = new FiltersResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		List<FiltersDTO> existingFilters = filtersService.getFiltersByUserId(userDTO);
		Type listType = new TypeToken<List<FiltersObjectResponseModel>>() {
		}.getType();
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(existingFilters, listType);
		filtersResponse.setFilters(responseObject);

		return filtersResponse;
	}

	@PostMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public FiltersResponseModel createFilters(@PathVariable String userId,
			@RequestBody FiltersRequestModel newFilters) {
		FiltersResponseModel newFiltersResponse = new FiltersResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		Type dtoListType = new TypeToken<List<FiltersDTO>>() {
		}.getType();
		List<FiltersDTO> filtersDTO = new ModelMapper().map(newFilters.getFilters(), dtoListType);
		List<FiltersDTO> createdFilters = filtersService.createFilters(userDTO, filtersDTO);

		Type responseListType = new TypeToken<List<FiltersObjectResponseModel>>() {
		}.getType();
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(createdFilters, responseListType);
		newFiltersResponse.setFilters(responseObject);

		return newFiltersResponse;
	}

	@PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public FiltersResponseModel updateFilters(@PathVariable String userId,
			@RequestBody FiltersRequestModel filtersToUpdate) {
		FiltersResponseModel updatedFiltersResponse = new FiltersResponseModel();

		UserDTO userDTO = userService.getUserByUserId(userId);
		Type dtoListType = new TypeToken<List<FiltersDTO>>() {
		}.getType();
		List<FiltersDTO> filtersDTO = new ModelMapper().map(filtersToUpdate.getFilters(), dtoListType);
		List<FiltersDTO> updatedFilters = filtersService.updateFilters(userDTO, filtersDTO);

		Type responseListType = new TypeToken<List<FiltersObjectResponseModel>>() {
		}.getType();
		List<FiltersObjectResponseModel> responseObject = new ModelMapper().map(updatedFilters, responseListType);
		updatedFiltersResponse.setFilters(responseObject);

		return updatedFiltersResponse;
	}

}
