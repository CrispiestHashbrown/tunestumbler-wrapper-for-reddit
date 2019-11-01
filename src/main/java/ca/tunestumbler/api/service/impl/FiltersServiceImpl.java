package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.FiltersRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.service.FiltersService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

@Service
public class FiltersServiceImpl implements FiltersService {

	private int idLength = 50;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FiltersRepository filtersRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public List<FiltersDTO> createFilters(UserDTO user, List<FiltersDTO> filters) {
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		Type entityListType = new TypeToken<List<FiltersEntity>>() {
		}.getType();
		List<FiltersEntity> newFilters = new ModelMapper().map(filters, entityListType);

		for (FiltersEntity newFilter : newFilters) {
			String filtersId = sharedUtils.generateFiltersId(idLength);
			newFilter.setFiltersId(filtersId);
			newFilter.setUserId(user.getUserId());
			newFilter.setLastModified(sharedUtils.getCurrentTime());
		}

		filtersRepository.saveAll(newFilters);
		Type dtoListType = new TypeToken<List<FiltersDTO>>() {
		}.getType();

		return new ModelMapper().map(newFilters, dtoListType);
	}

	@Override
	public List<FiltersDTO> getFiltersByUserId(UserDTO user) {
		String userId = user.getUserId();
		List<FiltersEntity> filtersList = filtersRepository.findFiltersByUserIdAndIsActive(userId);

		Type listType = new TypeToken<List<FiltersDTO>>() {
		}.getType();

		return new ModelMapper().map(filtersList, listType);
	}

	@Override
	public List<FiltersDTO> updateFilters(UserDTO user, List<FiltersDTO> filters) {
		String userId = user.getUserId();
		List<FiltersEntity> existingFilters = filtersRepository.findFiltersByUserIdAndIsActive(userId);
		HashMap<String, FiltersEntity> filtersMap = new HashMap<>();
		for (FiltersEntity filtersEntity : existingFilters) {
			filtersMap.put(filtersEntity.getFiltersId(), filtersEntity);
		}

		for (FiltersDTO filtersDTO : filters) {
			FiltersEntity filtersEntity = filtersMap.get(filtersDTO.getFiltersId());
			BeanUtils.copyProperties(filtersDTO, filtersEntity);
			filtersEntity.setLastModified(sharedUtils.getCurrentTime());
		}

		List<FiltersEntity> updatedFilters = filtersRepository.saveAll(existingFilters);
		Type dtoListType = new TypeToken<List<FiltersDTO>>() {
		}.getType();

		return new ModelMapper().map(updatedFilters, dtoListType);
	}

}
