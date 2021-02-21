package ca.tunestumbler.api.shared.mapper.impl;

import ca.tunestumbler.api.io.entity.ExcludedDomainEntity;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.ExcludedDomainDTO;
import ca.tunestumbler.api.shared.mapper.ExcludedDomainMapper;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateExcludedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateExcludedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.ExcludedDomainObjectResponseModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExcludedDomainMapperImpl implements ExcludedDomainMapper {

	private int length = 50;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public ExcludedDomainDTO createRequestObjectToExcludedDomainDTO(CreateExcludedDomainObjectRequestModel requestObject) {
		ExcludedDomainDTO dto = new ExcludedDomainDTO();
		dto.setExcludedDomain(requestObject.getExcludedDomain());

		return dto;
	}

	@Override
	public ExcludedDomainDTO updateRequestObjectToExcludedDomainDTO(UpdateExcludedDomainObjectRequestModel requestObject) {
		ExcludedDomainDTO dto = new ExcludedDomainDTO();
		dto.setExcludedDomainId(requestObject.getExcludedDomainId());
		dto.setExcludedDomain(requestObject.getExcludedDomain());

		return dto;
	}

    @Override
    public ExcludedDomainEntity excludedDomainDTOtoExcludedDomainEntity(ExcludedDomainDTO dto, String userId, String filtersId) {
        if ( dto == null ) {
            return null;
        }

        ExcludedDomainEntity excludedDomainEntity = new ExcludedDomainEntity();

        excludedDomainEntity.setExcludedDomainId( sharedUtils.generateFiltersId(length) );
        excludedDomainEntity.setUserId( userId );
        excludedDomainEntity.setFiltersId( filtersId );
        excludedDomainEntity.setExcludedDomain( dto.getExcludedDomain() );

        return excludedDomainEntity;
    }

	@Override
	public ExcludedDomainEntity updateExcludedDomainEntityFromExcludedDomainDTO(ExcludedDomainDTO dto, ExcludedDomainEntity entity) {
		if (dto == null) {
			return null;
		}

		entity.setExcludedDomain(dto.getExcludedDomain());

		return entity;
	}

    @Override
    public ExcludedDomainDTO excludedDomainEntityToExcludedDomainDTO(ExcludedDomainEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ExcludedDomainDTO excludedDomainDTO = new ExcludedDomainDTO();

        excludedDomainDTO.setExcludedDomainId( entity.getExcludedDomainId() );
        excludedDomainDTO.setFiltersId( entity.getFiltersId() );
        excludedDomainDTO.setExcludedDomain( entity.getExcludedDomain() );

        return excludedDomainDTO;
    }

    @Override
    public ExcludedDomainObjectResponseModel excludedDomainDTOtoResponseObject(ExcludedDomainDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ExcludedDomainObjectResponseModel excludedDomainObjectResponseModel = new ExcludedDomainObjectResponseModel();

        excludedDomainObjectResponseModel.setExcludedDomainId( dto.getExcludedDomainId() );
        excludedDomainObjectResponseModel.setExcludedDomain( dto.getExcludedDomain() );

        return excludedDomainObjectResponseModel;
    }

    @Override
    public List<ExcludedDomainDTO> createRequestObjectsToExcludedDomainDTOs(List<CreateExcludedDomainObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainDTO> list = new ArrayList<>( requestObjects.size() );
        for ( CreateExcludedDomainObjectRequestModel createExcludedDomainObjectRequestModel : requestObjects ) {
            list.add( createRequestObjectToExcludedDomainDTO( createExcludedDomainObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<ExcludedDomainDTO> updateRequestObjectsToExcludedDomainDTOs(List<UpdateExcludedDomainObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainDTO> list = new ArrayList<>( requestObjects.size() );
        for ( UpdateExcludedDomainObjectRequestModel updateExcludedDomainObjectRequestModel : requestObjects ) {
            list.add( updateRequestObjectToExcludedDomainDTO( updateExcludedDomainObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<ExcludedDomainEntity> excludedDomainDTOlistToExcludedDomainEntityList(List<ExcludedDomainDTO> dtoList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainEntity> list = new ArrayList<>( dtoList.size() );
        for ( ExcludedDomainDTO excludedDomainDTO : dtoList ) {
            list.add( excludedDomainDTOtoExcludedDomainEntity( excludedDomainDTO, userId, filtersId ) );
        }

        return list;
    }

	@Override
	public List<ExcludedDomainEntity> updateExcludedDomainEntityListFromExcludedDomainDTOlist(List<ExcludedDomainDTO> dtoList, List<ExcludedDomainEntity> entityList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return entityList;
        }

        Map<String, ExcludedDomainEntity> excludedDomainEntityMap = createExcludedDomainEntityMap(entityList);
        entityList.clear();
        for ( ExcludedDomainDTO dto : dtoList) {
        	if ( !excludedDomainEntityMap.containsKey(dto.getExcludedDomainId() ) ) {
        		entityList.add( excludedDomainDTOtoExcludedDomainEntity( dto, userId, filtersId ));
        	} else {
        		entityList.add( updateExcludedDomainEntityFromExcludedDomainDTO( dto, excludedDomainEntityMap.get(dto.getExcludedDomainId()) ) );
        		excludedDomainEntityMap.remove( dto.getExcludedDomainId() );
			}
		}
        for ( Map.Entry<String, ExcludedDomainEntity> entry : excludedDomainEntityMap.entrySet() ) {
        	entityList.add(entry.getValue());
        }

		return entityList;
	}

    @Override
    public List<ExcludedDomainDTO> excludedDomainEntityListToExcludedDomainDTOlist(List<ExcludedDomainEntity> entityList) {
        if ( entityList == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainDTO> list = new ArrayList<>( entityList.size() );
        for ( ExcludedDomainEntity excludedDomainEntity : entityList ) {
            list.add( excludedDomainEntityToExcludedDomainDTO( excludedDomainEntity ) );
        }

        return list;
    }

    @Override
    public List<ExcludedDomainObjectResponseModel> excludedDomainDTOlistToResponseObjects(List<ExcludedDomainDTO> dtoList) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainObjectResponseModel> list = new ArrayList<>( dtoList.size() );
        for ( ExcludedDomainDTO excludedDomainDTO : dtoList ) {
            list.add( excludedDomainDTOtoResponseObject( excludedDomainDTO ) );
        }

        return list;
    }

    protected ExcludedDomainDTO createExcludedDomainObjectRequestModelToExcludedDomainDTO(CreateExcludedDomainObjectRequestModel createExcludedDomainObjectRequestModel) {
        if ( createExcludedDomainObjectRequestModel == null ) {
            return null;
        }

        ExcludedDomainDTO excludedDomainDTO = new ExcludedDomainDTO();

        excludedDomainDTO.setExcludedDomain( createExcludedDomainObjectRequestModel.getExcludedDomain() );

        return excludedDomainDTO;
    }

    protected ExcludedDomainDTO updateExcludedDomainObjectRequestModelToExcludedDomainDTO(UpdateExcludedDomainObjectRequestModel updateExcludedDomainObjectRequestModel, String filtersId) {
        if ( updateExcludedDomainObjectRequestModel == null ) {
            return null;
        }

        ExcludedDomainDTO excludedDomainDTO = new ExcludedDomainDTO();

        excludedDomainDTO.setExcludedDomainId( updateExcludedDomainObjectRequestModel.getExcludedDomainId() );
        excludedDomainDTO.setFiltersId( filtersId );
        excludedDomainDTO.setExcludedDomain( updateExcludedDomainObjectRequestModel.getExcludedDomain() );

        return excludedDomainDTO;
    }

	private Map<String, ExcludedDomainEntity> createExcludedDomainEntityMap(List<ExcludedDomainEntity> excludedDomains) {
		Map<String, ExcludedDomainEntity> excludedDomainEntityMap = new HashMap<>();
		for (ExcludedDomainEntity excludedDomain : excludedDomains) {
			excludedDomainEntityMap.put(excludedDomain.getExcludedDomainId(), excludedDomain);
		}
		return excludedDomainEntityMap;
	}
}
