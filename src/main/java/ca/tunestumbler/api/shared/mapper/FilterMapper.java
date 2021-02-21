package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateFiltersObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateFiltersObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.FiltersObjectResponseModel;

public interface FilterMapper {

	FiltersDTO createRequestObjectToFiltersDTO(CreateFiltersObjectRequestModel requestObject);

	FiltersDTO updateRequestObjectToFiltersDTO(UpdateFiltersObjectRequestModel requestObject);

	FiltersEntity filtersDTOToFiltersEntity(FiltersDTO dto, String userId);

	FiltersEntity updateFiltersEntityFromFiltersDTO(FiltersDTO dto, FiltersEntity entity);

	FiltersDTO filtersEntityToFiltersDTO(FiltersEntity entity);

	FiltersObjectResponseModel filtersDTOtoResponseObject(FiltersDTO dto);

	List<FiltersDTO> createRequestObjectsToFiltersDTOs(List<CreateFiltersObjectRequestModel> requestObjects);

	List<FiltersDTO> updateRequestObjectsToFiltersDTOs(List<UpdateFiltersObjectRequestModel> requestObjects);

	List<FiltersEntity> filtersDTOlistToFiltersEntityList(List<FiltersDTO> dtoList, String userId);

	List<FiltersEntity> updateFiltersEntityListFromFiltersDTOlist(List<FiltersDTO> dtoList,
			List<FiltersEntity> entityList, String userId);

	List<FiltersDTO> filtersEntityListToFiltersDTOlist(List<FiltersEntity> entityList);

	List<FiltersObjectResponseModel> filtersDTOlistToResponseObjects(List<FiltersDTO> dtoList);

}
