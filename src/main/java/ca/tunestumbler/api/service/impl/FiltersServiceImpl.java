package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.exceptions.FiltersNotFoundException;
import ca.tunestumbler.api.exceptions.InvalidBodyException;
import ca.tunestumbler.api.exceptions.ResourceNotFoundException;
import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.io.entity.SelectedDomainEntity;
import ca.tunestumbler.api.io.entity.SelectedKeywordEntity;
import ca.tunestumbler.api.io.entity.ExcludedDomainEntity;
import ca.tunestumbler.api.io.entity.ExcludedKeywordEntity;
import ca.tunestumbler.api.io.repositories.FiltersRepository;
import ca.tunestumbler.api.io.repositories.ExcludedDomainRepository;
import ca.tunestumbler.api.io.repositories.ExcludedKeywordRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.io.repositories.SelectedDomainRepository;
import ca.tunestumbler.api.io.repositories.SelectedKeywordRepository;
import ca.tunestumbler.api.service.FiltersService;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.mapper.FilterMapper;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;
import ca.tunestumbler.api.ui.model.response.ErrorPrefixes;

@Service
public class FiltersServiceImpl implements FiltersService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FiltersRepository filtersRepository;

	@Autowired
	ExcludedDomainRepository excludedDomainRepository;

	@Autowired
	ExcludedKeywordRepository excludedKeywordRepository;

	@Autowired
	SelectedDomainRepository selectedDomainRepository;

	@Autowired
	SelectedKeywordRepository selectedKeywordRepository;

	@Autowired
	FilterMapper filterMapper;

	@Override
	public List<FiltersDTO> getFiltersByUserId(String userId) {
		List<FiltersEntity> filtersList = filtersRepository.findFiltersByUserId(userId);
		return filterMapper.filtersEntityListToFiltersDTOlist(filtersList);
	}

	@Override
	public List<FiltersDTO> createFilters(String userId, List<FiltersDTO> filters) {
		Set<String> filterSubreddits = filtersRepository.findSubredditsByUserId(userId);
		validateForSubredditUniqueness(filterSubreddits, filters);
		List<FiltersEntity> newFilters = filterMapper.filtersDTOlistToFiltersEntityList(filters, userId);
		List<FiltersEntity> savedFilters = filtersRepository.saveAll(newFilters);
		return filterMapper.filtersEntityListToFiltersDTOlist(savedFilters);
	}

	@Override
	public List<FiltersDTO> updateFilters(String userId, List<FiltersDTO> filters) {
		List<String> filterIds = createListOfFilterDTOids(filters);
		List<FiltersEntity> outdatedFilters = filtersRepository.findFiltersByFilterIdListAndUserId(filterIds, userId);
		if (outdatedFilters.isEmpty()) {
			throw new FiltersNotFoundException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.FILTER_RESOURCES_NOT_FOUND.getErrorMessage());
		}
		List<FiltersEntity> updatedFilters = filterMapper.updateFiltersEntityListFromFiltersDTOlist(filters,
				outdatedFilters, userId);
		List<FiltersEntity> savedFilters = filtersRepository.saveAll(updatedFilters);
		return filterMapper.filtersEntityListToFiltersDTOlist(savedFilters);
	}

	@Override
	public void deleteFilters(String userId, List<String> filters) {
		List<FiltersEntity> filterEntities = filtersRepository.findFiltersByFilterIdListAndUserId(filters, userId);

		filtersRepository.deleteAll(filterEntities);
	}

	@Override
	public void deleteExcludedDomains(String userId, List<String> excludedDomainIds) {
		List<ExcludedDomainEntity> excludedDomainEntities = excludedDomainRepository
				.findAllExcludedDomainsByExcludedDomainIdsAndUserId(excludedDomainIds, userId);
		if (excludedDomainEntities.isEmpty()) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		excludedDomainRepository.deleteAll(excludedDomainEntities);
	}

	@Override
	public void deleteExcludedKeywords(String userId, List<String> excludedKeywordIds) {
		List<ExcludedKeywordEntity> excludedKeywordEntities = excludedKeywordRepository
				.findAllExcludedKeywordsByExcludedKeywordIdsAndUserId(excludedKeywordIds, userId);
		if (excludedKeywordEntities.isEmpty()) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		excludedKeywordRepository.deleteAll(excludedKeywordEntities);
	}

	@Override
	public void deleteSelectedDomains(String userId, List<String> selectedDomainIds) {
		List<SelectedDomainEntity> selectedDomainEntities = selectedDomainRepository
				.findAllSelectedDomainsBySelectedDomainIdsAndUserId(selectedDomainIds, userId);
		if (selectedDomainEntities.isEmpty()) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		selectedDomainRepository.deleteAll(selectedDomainEntities);
	}

	@Override
	public void deleteSelectedKeywords(String userId, List<String> selectedKeywordIds) {
		List<SelectedKeywordEntity> selectedKeywordEntities = selectedKeywordRepository
				.findAllSelectedKeywordsBySelectedKeywordIdsAndUserId(selectedKeywordIds, userId);
		if (selectedKeywordEntities.isEmpty()) {
			throw new ResourceNotFoundException(
					ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix() + ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		selectedKeywordRepository.deleteAll(selectedKeywordEntities);
	}

	private List<String> createListOfFilterDTOids(List<FiltersDTO> filters) {
		List<String> filterIds = new ArrayList<>();
		for (FiltersDTO updatedFilter : filters) {
			filterIds.add(updatedFilter.getFiltersId());
		}
		return filterIds;
	}

	private void validateForSubredditUniqueness(Set<String> subreddits, List<FiltersDTO> filters) {
		StringBuilder duplicateSubreddits = new StringBuilder();
		for (FiltersDTO filter : filters) {
			if (subreddits.contains(filter.getSubreddit())) {
				duplicateSubreddits.append("Filter already exists for subreddit: " + filter.getSubreddit() + ". ");
			}
		}
		if (duplicateSubreddits.length() > 0) {
			throw new InvalidBodyException(ErrorPrefixes.FILTERS_SERVICE.getErrorPrefix()
					+ ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage() + duplicateSubreddits);
		}
	}

}
