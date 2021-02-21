package ca.tunestumbler.api.shared.mapper.impl;

import ca.tunestumbler.api.io.entity.SelectedDomainEntity;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SelectedDomainDTO;
import ca.tunestumbler.api.shared.mapper.SelectedDomainMapper;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateSelectedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateSelectedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.SelectedDomainObjectResponseModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectedDomainMapperImpl implements SelectedDomainMapper {

	private int length = 50;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public SelectedDomainDTO createRequestObjectToSelectedDomainDTO(CreateSelectedDomainObjectRequestModel requestObject) {
		SelectedDomainDTO dto = new SelectedDomainDTO();
		dto.setSelectedDomain(requestObject.getSelectedDomain());

		return dto;
	}

	@Override
	public SelectedDomainDTO updateRequestObjectToSelectedDomainDTO(UpdateSelectedDomainObjectRequestModel requestObject) {
		SelectedDomainDTO dto = new SelectedDomainDTO();
		dto.setSelectedDomainId(requestObject.getSelectedDomainId());
		dto.setSelectedDomain(requestObject.getSelectedDomain());

		return dto;
	}
	
    @Override
    public SelectedDomainEntity selectedDomainDTOtoSelectedDomainEntity(SelectedDomainDTO dto, String userId, String filtersId) {
        if ( dto == null ) {
            return null;
        }

        SelectedDomainEntity selectedDomainEntity = new SelectedDomainEntity();

        selectedDomainEntity.setSelectedDomainId( sharedUtils.generateFiltersId(length) );
        selectedDomainEntity.setUserId( userId );
        selectedDomainEntity.setFiltersId( filtersId );
        selectedDomainEntity.setSelectedDomain( dto.getSelectedDomain() );

        return selectedDomainEntity;
    }

	@Override
	public SelectedDomainEntity updateSelectedDomainEntityFromSelectedDomainDTO(SelectedDomainDTO dto, SelectedDomainEntity entity) {
		if (dto == null) {
			return null;
		}

		entity.setSelectedDomain(dto.getSelectedDomain());

		return entity;
	}

    @Override
    public SelectedDomainDTO selectedDomainEntityToSelectedDomainDTO(SelectedDomainEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SelectedDomainDTO selectedDomainDTO = new SelectedDomainDTO();

        selectedDomainDTO.setSelectedDomainId( entity.getSelectedDomainId() );
        selectedDomainDTO.setFiltersId( entity.getFiltersId() );
        selectedDomainDTO.setSelectedDomain( entity.getSelectedDomain() );

        return selectedDomainDTO;
    }

    @Override
    public SelectedDomainObjectResponseModel selectedDomainDTOtoResponseObject(SelectedDomainDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SelectedDomainObjectResponseModel selectedDomainObjectResponseModel = new SelectedDomainObjectResponseModel();

        selectedDomainObjectResponseModel.setSelectedDomainId( dto.getSelectedDomainId() );
        selectedDomainObjectResponseModel.setSelectedDomain( dto.getSelectedDomain() );

        return selectedDomainObjectResponseModel;
    }

    @Override
    public List<SelectedDomainDTO> createRequestObjectsToSelectedDomainDTOs(List<CreateSelectedDomainObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainDTO> list = new ArrayList<>( requestObjects.size() );
        for ( CreateSelectedDomainObjectRequestModel createSelectedDomainObjectRequestModel : requestObjects ) {
            list.add( createRequestObjectToSelectedDomainDTO( createSelectedDomainObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<SelectedDomainDTO> updateRequestObjectsToSelectedDomainDTOs(List<UpdateSelectedDomainObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainDTO> list = new ArrayList<>( requestObjects.size() );
        for ( UpdateSelectedDomainObjectRequestModel updateSelectedDomainObjectRequestModel : requestObjects ) {
            list.add( updateRequestObjectToSelectedDomainDTO( updateSelectedDomainObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<SelectedDomainEntity> selectedDomainDTOlistToSelectedDomainEntityList(List<SelectedDomainDTO> dtoList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainEntity> list = new ArrayList<>( dtoList.size() );
        for ( SelectedDomainDTO selectedDomainDTO : dtoList ) {
            list.add( selectedDomainDTOtoSelectedDomainEntity( selectedDomainDTO, userId, filtersId ) );
        }

        return list;
    }

	@Override
	public List<SelectedDomainEntity> updateSelectedDomainEntityListFromSelectedDomainDTOlist(List<SelectedDomainDTO> dtoList, List<SelectedDomainEntity> entityList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return entityList;
        }

        Map<String, SelectedDomainEntity> selectedDomainEntityMap = createSelectedDomainEntityMap(entityList);
        entityList.clear();
        for ( SelectedDomainDTO dto : dtoList) {
        	if ( !selectedDomainEntityMap.containsKey(dto.getSelectedDomainId()) ) {
        		entityList.add( selectedDomainDTOtoSelectedDomainEntity( dto, userId, filtersId ));
        	} else {
        		entityList.add( updateSelectedDomainEntityFromSelectedDomainDTO( dto, selectedDomainEntityMap.get(dto.getSelectedDomainId()) ) );
        		selectedDomainEntityMap.remove( dto.getSelectedDomainId() );
			}
		}
        for ( Map.Entry<String, SelectedDomainEntity> entry : selectedDomainEntityMap.entrySet() ) {
        	entityList.add(entry.getValue());
		}

		return entityList;
	}

    @Override
    public List<SelectedDomainDTO> selectedDomainEntityListToSelectedDomainDTOlist(List<SelectedDomainEntity> entityList) {
        if ( entityList == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainDTO> list = new ArrayList<>( entityList.size() );
        for ( SelectedDomainEntity selectedDomainEntity : entityList ) {
            list.add( selectedDomainEntityToSelectedDomainDTO( selectedDomainEntity ) );
        }

        return list;
    }

    @Override
    public List<SelectedDomainObjectResponseModel> selectedDomainDTOlistToResponseObjects(List<SelectedDomainDTO> dtoList) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainObjectResponseModel> list = new ArrayList<>( dtoList.size() );
        for ( SelectedDomainDTO selectedDomainDTO : dtoList ) {
            list.add( selectedDomainDTOtoResponseObject( selectedDomainDTO ) );
        }

        return list;
    }

    protected SelectedDomainDTO createSelectedDomainObjectRequestModelToSelectedDomainDTO(CreateSelectedDomainObjectRequestModel createSelectedDomainObjectRequestModel) {
        if ( createSelectedDomainObjectRequestModel == null ) {
            return null;
        }

        SelectedDomainDTO selectedDomainDTO = new SelectedDomainDTO();

        selectedDomainDTO.setSelectedDomain( createSelectedDomainObjectRequestModel.getSelectedDomain() );

        return selectedDomainDTO;
    }

    protected SelectedDomainDTO updateSelectedDomainObjectRequestModelToSelectedDomainDTO(UpdateSelectedDomainObjectRequestModel updateSelectedDomainObjectRequestModel, String filtersId) {
        if ( updateSelectedDomainObjectRequestModel == null ) {
            return null;
        }

        SelectedDomainDTO selectedDomainDTO = new SelectedDomainDTO();

        selectedDomainDTO.setSelectedDomainId( updateSelectedDomainObjectRequestModel.getSelectedDomainId() );
        selectedDomainDTO.setFiltersId( filtersId );
        selectedDomainDTO.setSelectedDomain( updateSelectedDomainObjectRequestModel.getSelectedDomain() );

        return selectedDomainDTO;
    }

	private Map<String, SelectedDomainEntity> createSelectedDomainEntityMap(List<SelectedDomainEntity> selectedDomains) {
		Map<String, SelectedDomainEntity> selectedDomainEntityMap = new HashMap<>();
		for (SelectedDomainEntity selectedDomain : selectedDomains) {
			selectedDomainEntityMap.put(selectedDomain.getSelectedDomainId(), selectedDomain);
		}
		return selectedDomainEntityMap;
	}
}
