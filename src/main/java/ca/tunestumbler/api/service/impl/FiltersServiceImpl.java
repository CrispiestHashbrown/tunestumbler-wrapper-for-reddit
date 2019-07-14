package ca.tunestumbler.api.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.exceptions.FiltersNotFoundException;
import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.FiltersRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.service.FiltersService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

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

		Long userMaxId = filtersRepository.findMaxIdByUserId(user.getUserId());
		Long maxId = filtersRepository.findMaxId();
		Long startId = sharedUtils.setStartId(userMaxId, maxId);
		for (FiltersEntity newFilter : newFilters) {
			String filtersId = sharedUtils.generateFiltersId(idLength);
			newFilter.setFiltersId(filtersId);
			newFilter.setUserEntity(userEntity);
			newFilter.setUserId(user.getUserId());
			newFilter.setStartId(startId);
			newFilter.setIsActive(true);
			newFilter.setLastModified(sharedUtils.getCurrentTime());
		}

		List<FiltersEntity> storedFiltersEntities = filtersRepository.saveAll(newFilters);
		Type dtoListType = new TypeToken<List<FiltersDTO>>() {
		}.getType();

		return new ModelMapper().map(storedFiltersEntities, dtoListType);
	}

	@Override
	public List<FiltersDTO> getFiltersByUserId(UserDTO user) {
		String userId = user.getUserId();
		Long startId = filtersRepository.findMaxStartIdByUserId(userId);
		List<FiltersEntity> filtersList = filtersRepository.findFiltersByUserIdAndStartIdAndIsActive(userId, startId);

		if (filtersList == null) {
			throw new FiltersNotFoundException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}

		Type listType = new TypeToken<List<FiltersDTO>>() {
		}.getType();

		return new ModelMapper().map(filtersList, listType);
	}

	@Override
	public List<FiltersDTO> updateFilters(UserDTO user, List<FiltersDTO> filters) {
		String userId = user.getUserId();
		Long startId = filtersRepository.findMaxStartIdByUserId(userId);
		List<FiltersEntity> existingFilters = filtersRepository.findFiltersByUserIdAndStartIdAndIsActive(userId,
				startId);

		for (FiltersEntity filtersEntity : existingFilters) {
			filtersEntity.setIsActive(false);
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		List<FiltersEntity> updatedFiltersEntities = new ArrayList<>();
		for (FiltersDTO filtersDTO : filters) {
			String filtersId = filtersDTO.getFiltersId();
			Boolean isExistingFilter = false;
			for (FiltersEntity filtersEntity : existingFilters) {

				// If the filter exists, update all filter fields.
				if (filtersId.equals(filtersEntity.getFiltersId())) {
					isExistingFilter = true;
					filtersEntity.setPriority(filtersDTO.getPriority());
					filtersEntity.setMinScore(filtersDTO.getMinScore());
					filtersEntity.setAllowNSFWFlag(filtersDTO.getAllowNSFWFlag());
					filtersEntity.setHideByKeyword(filtersDTO.getHideByKeyword());
					filtersEntity.setShowByKeyword(filtersDTO.getShowByKeyword());
					filtersEntity.setHideByDomain(filtersDTO.getHideByDomain());
					filtersEntity.setShowByDomain(filtersDTO.getShowByDomain());
					filtersEntity.setIsActive(true);
					filtersEntity.setLastModified(sharedUtils.getCurrentTime());
					updatedFiltersEntities.add(filtersEntity);
					break;
				}
			}

			// If the filter does not exist, add the filter
			if (!isExistingFilter) {
				FiltersEntity newFilter = new FiltersEntity();
				String newFiltersId = sharedUtils.generateFiltersId(idLength);
				newFilter.setFiltersId(newFiltersId);
				newFilter.setUserEntity(userEntity);
				newFilter.setUserId(userId);
				newFilter.setMultireddit(filtersDTO.getMultireddit());
				newFilter.setSubreddit(filtersDTO.getSubreddit());
				newFilter.setPriority(filtersDTO.getPriority());
				newFilter.setMinScore(filtersDTO.getMinScore());
				newFilter.setAllowNSFWFlag(filtersDTO.getAllowNSFWFlag());
				newFilter.setHideByKeyword(filtersDTO.getHideByKeyword());
				newFilter.setShowByKeyword(filtersDTO.getShowByKeyword());
				newFilter.setHideByDomain(filtersDTO.getHideByDomain());
				newFilter.setShowByDomain(filtersDTO.getShowByDomain());
				newFilter.setStartId(startId);
				newFilter.setIsActive(true);
				newFilter.setLastModified(sharedUtils.getCurrentTime());
				updatedFiltersEntities.add(newFilter);
			}
		}

		List<FiltersEntity> updatedFilters = filtersRepository.saveAll(updatedFiltersEntities);
		Type dtoListType = new TypeToken<List<FiltersDTO>>() {
		}.getType();

		return new ModelMapper().map(updatedFilters, dtoListType);
	}

}
