package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.ExcludedKeywordEntity;
import ca.tunestumbler.api.shared.dto.ExcludedKeywordDTO;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateExcludedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateExcludedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.ExcludedKeywordObjectResponseModel;

public interface ExcludedKeywordMapper {

	ExcludedKeywordDTO createRequestObjectToExcludedKeywordDTO(CreateExcludedKeywordObjectRequestModel requestObject);

	ExcludedKeywordDTO updateRequestObjectToExcludedKeywordDTO(UpdateExcludedKeywordObjectRequestModel requestObject);

	ExcludedKeywordEntity excludedKeywordDTOtoExcludedKeywordEntity(ExcludedKeywordDTO dto, String userId,
			String filtersId);

	ExcludedKeywordEntity updateExcludedKeywordEntityFromExcludedKeywordDTO(ExcludedKeywordDTO dto,
			ExcludedKeywordEntity entity);

	ExcludedKeywordDTO excludedKeywordEntityToExcludedKeywordDTO(ExcludedKeywordEntity entity);

	ExcludedKeywordObjectResponseModel excludedKeywordDTOtoResponseObject(ExcludedKeywordDTO dto);

	List<ExcludedKeywordDTO> createRequestObjectsToExcludedKeywordDTOs(
			List<CreateExcludedKeywordObjectRequestModel> requestObjects);

	List<ExcludedKeywordDTO> updateRequestObjectsToExcludedKeywordDTOs(
			List<UpdateExcludedKeywordObjectRequestModel> requestObjects);

	List<ExcludedKeywordEntity> excludedKeywordDTOlistToExcludedKeywordEntityList(List<ExcludedKeywordDTO> dtoList,
			String userId, String filtersId);

	List<ExcludedKeywordEntity> updateExcludedKeywordEntityListFromExcludedKeywordDTOlist(
			List<ExcludedKeywordDTO> dtoList, List<ExcludedKeywordEntity> entityList, String userId, String filtersId);

	List<ExcludedKeywordDTO> excludedKeywordEntityListToExcludedKeywordDTOlist(List<ExcludedKeywordEntity> entityList);

	List<ExcludedKeywordObjectResponseModel> excludedKeywordDTOlistToResponseObjects(List<ExcludedKeywordDTO> dtoList);

}
