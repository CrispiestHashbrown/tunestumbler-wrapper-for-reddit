package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.SelectedKeywordEntity;
import ca.tunestumbler.api.shared.dto.SelectedKeywordDTO;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateSelectedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateSelectedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.SelectedKeywordObjectResponseModel;

public interface SelectedKeywordMapper {

	SelectedKeywordDTO createRequestObjectToSelectedKeywordDTO(CreateSelectedKeywordObjectRequestModel requestObject);

	SelectedKeywordDTO updateRequestObjectToSelectedKeywordDTO(UpdateSelectedKeywordObjectRequestModel requestObject);

	SelectedKeywordEntity selectedKeywordDTOtoSelectedKeywordEntity(SelectedKeywordDTO dto, String userId,
			String filtersId);

	SelectedKeywordEntity updateSelectedKeywordEntityFromSelectedKeywordDTO(SelectedKeywordDTO dto,
			SelectedKeywordEntity entity);

	SelectedKeywordDTO selectedKeywordEntityToSelectedKeywordDTO(SelectedKeywordEntity entity);

	SelectedKeywordObjectResponseModel selectedKeywordDTOtoResponseObject(SelectedKeywordDTO dto);

	List<SelectedKeywordDTO> createRequestObjectsToSelectedKeywordDTOs(
			List<CreateSelectedKeywordObjectRequestModel> requestObjects);

	List<SelectedKeywordDTO> updateRequestObjectsToSelectedKeywordDTOs(
			List<UpdateSelectedKeywordObjectRequestModel> requestObjects);

	List<SelectedKeywordEntity> selectedKeywordDTOlistToSelectedKeywordEntityList(List<SelectedKeywordDTO> dtoList,
			String userId, String filtersId);

	List<SelectedKeywordEntity> updateSelectedKeywordEntityListFromSelectedKeywordDTOlist(
			List<SelectedKeywordDTO> dtoList, List<SelectedKeywordEntity> entityList, String userId, String filtersId);

	List<SelectedKeywordDTO> selectedKeywordEntityListToSelectedKeywordDTOlist(List<SelectedKeywordEntity> entityList);

	List<SelectedKeywordObjectResponseModel> selectedKeywordDTOlistToResponseObjects(List<SelectedKeywordDTO> dtoList);

}
