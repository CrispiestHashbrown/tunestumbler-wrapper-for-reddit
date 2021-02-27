package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.ExcludedDomainEntity;
import ca.tunestumbler.api.shared.dto.ExcludedDomainDTO;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateExcludedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateExcludedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.ExcludedDomainObjectResponseModel;

public interface ExcludedDomainMapper {

	ExcludedDomainDTO createRequestObjectToExcludedDomainDTO(CreateExcludedDomainObjectRequestModel requestObject);

	ExcludedDomainDTO updateRequestObjectToExcludedDomainDTO(UpdateExcludedDomainObjectRequestModel requestObject);

	ExcludedDomainEntity excludedDomainDTOtoExcludedDomainEntity(ExcludedDomainDTO dto, String userId,
			String filtersId);

	ExcludedDomainEntity updateExcludedDomainEntityFromExcludedDomainDTO(ExcludedDomainDTO dto,
			ExcludedDomainEntity entity);

	ExcludedDomainDTO excludedDomainEntityToExcludedDomainDTO(ExcludedDomainEntity entity);

	ExcludedDomainObjectResponseModel excludedDomainDTOtoResponseObject(ExcludedDomainDTO dto);

	List<ExcludedDomainDTO> createRequestObjectsToExcludedDomainDTOs(
			List<CreateExcludedDomainObjectRequestModel> requestObjects);

	List<ExcludedDomainDTO> updateRequestObjectsToExcludedDomainDTOs(
			List<UpdateExcludedDomainObjectRequestModel> requestObjects);

	List<ExcludedDomainEntity> excludedDomainDTOlistToExcludedDomainEntityList(List<ExcludedDomainDTO> dtoList,
			String userId, String filtersId);

	List<ExcludedDomainEntity> updateExcludedDomainEntityListFromExcludedDomainDTOlist(List<ExcludedDomainDTO> dtoList,
			List<ExcludedDomainEntity> entityList, String userId, String filtersId);

	List<ExcludedDomainDTO> excludedDomainEntityListToExcludedDomainDTOlist(List<ExcludedDomainEntity> entityList);

	List<ExcludedDomainObjectResponseModel> excludedDomainDTOlistToResponseObjects(List<ExcludedDomainDTO> dtoList);

}
