package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.SelectedDomainEntity;
import ca.tunestumbler.api.shared.dto.SelectedDomainDTO;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateSelectedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateSelectedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.SelectedDomainObjectResponseModel;

public interface SelectedDomainMapper {

	SelectedDomainDTO createRequestObjectToSelectedDomainDTO(CreateSelectedDomainObjectRequestModel requestObject);

	SelectedDomainDTO updateRequestObjectToSelectedDomainDTO(UpdateSelectedDomainObjectRequestModel requestObject);

	SelectedDomainEntity selectedDomainDTOtoSelectedDomainEntity(SelectedDomainDTO dto, String userId,
			String filtersId);

	SelectedDomainEntity updateSelectedDomainEntityFromSelectedDomainDTO(SelectedDomainDTO dto,
			SelectedDomainEntity entity);

	SelectedDomainDTO selectedDomainEntityToSelectedDomainDTO(SelectedDomainEntity entity);

	SelectedDomainObjectResponseModel selectedDomainDTOtoResponseObject(SelectedDomainDTO dto);

	List<SelectedDomainDTO> createRequestObjectsToSelectedDomainDTOs(
			List<CreateSelectedDomainObjectRequestModel> requestObjects);

	List<SelectedDomainDTO> updateRequestObjectsToSelectedDomainDTOs(
			List<UpdateSelectedDomainObjectRequestModel> requestObjects);

	List<SelectedDomainEntity> selectedDomainDTOlistToSelectedDomainEntityList(List<SelectedDomainDTO> dtoList,
			String userId, String filtersId);

	List<SelectedDomainEntity> updateSelectedDomainEntityListFromSelectedDomainDTOlist(List<SelectedDomainDTO> dtoList,
			List<SelectedDomainEntity> entityList, String userId, String filtersId);

	List<SelectedDomainDTO> selectedDomainEntityListToSelectedDomainDTOlist(List<SelectedDomainEntity> entityList);

	List<SelectedDomainObjectResponseModel> selectedDomainDTOlistToResponseObjects(List<SelectedDomainDTO> dtoList);

}
