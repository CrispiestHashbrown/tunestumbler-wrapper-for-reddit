package ca.tunestumbler.api.shared.mapper.impl;

import ca.tunestumbler.api.io.entity.ExcludedKeywordEntity;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.ExcludedKeywordDTO;
import ca.tunestumbler.api.shared.mapper.ExcludedKeywordMapper;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateExcludedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateExcludedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.ExcludedKeywordObjectResponseModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExcludedKeywordMapperImpl implements ExcludedKeywordMapper {

	private int length = 50;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public ExcludedKeywordDTO createRequestObjectToExcludedKeywordDTO(CreateExcludedKeywordObjectRequestModel requestObject) {
		ExcludedKeywordDTO dto = new ExcludedKeywordDTO();
		dto.setExcludedKeyword(requestObject.getExcludedKeyword());

		return dto;
	}

	@Override
	public ExcludedKeywordDTO updateRequestObjectToExcludedKeywordDTO(UpdateExcludedKeywordObjectRequestModel requestObject) {
		ExcludedKeywordDTO dto = new ExcludedKeywordDTO();
		dto.setExcludedKeywordId(requestObject.getExcludedKeywordId());
		dto.setExcludedKeyword(requestObject.getExcludedKeyword());

		return dto;
	}

    @Override
    public ExcludedKeywordEntity excludedKeywordDTOtoExcludedKeywordEntity(ExcludedKeywordDTO dto, String userId, String filtersId) {
        if ( dto == null ) {
            return null;
        }

        ExcludedKeywordEntity excludedKeywordEntity = new ExcludedKeywordEntity();

        excludedKeywordEntity.setExcludedKeywordId( sharedUtils.generateFiltersId(length) );
        excludedKeywordEntity.setUserId( userId );
        excludedKeywordEntity.setFiltersId( filtersId );
        excludedKeywordEntity.setExcludedKeyword( dto.getExcludedKeyword() );

        return excludedKeywordEntity;
    }

	@Override
	public ExcludedKeywordEntity updateExcludedKeywordEntityFromExcludedKeywordDTO(ExcludedKeywordDTO dto, ExcludedKeywordEntity entity) {
		if (dto == null) {
			return null;
		}

		entity.setExcludedKeyword(dto.getExcludedKeyword());

		return entity;
	}

    @Override
    public ExcludedKeywordDTO excludedKeywordEntityToExcludedKeywordDTO(ExcludedKeywordEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ExcludedKeywordDTO excludedKeywordDTO = new ExcludedKeywordDTO();

        excludedKeywordDTO.setExcludedKeywordId( entity.getExcludedKeywordId() );
        excludedKeywordDTO.setFiltersId( entity.getFiltersId() );
        excludedKeywordDTO.setExcludedKeyword( entity.getExcludedKeyword() );

        return excludedKeywordDTO;
    }

    @Override
    public ExcludedKeywordObjectResponseModel excludedKeywordDTOtoResponseObject(ExcludedKeywordDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ExcludedKeywordObjectResponseModel excludedKeywordObjectResponseModel = new ExcludedKeywordObjectResponseModel();

        excludedKeywordObjectResponseModel.setExcludedKeywordId( dto.getExcludedKeywordId() );
        excludedKeywordObjectResponseModel.setExcludedKeyword( dto.getExcludedKeyword() );

        return excludedKeywordObjectResponseModel;
    }

    @Override
    public List<ExcludedKeywordDTO> createRequestObjectsToExcludedKeywordDTOs(List<CreateExcludedKeywordObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordDTO> list = new ArrayList<>( requestObjects.size() );
        for ( CreateExcludedKeywordObjectRequestModel createExcludedKeywordObjectRequestModel : requestObjects ) {
            list.add( createRequestObjectToExcludedKeywordDTO( createExcludedKeywordObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<ExcludedKeywordDTO> updateRequestObjectsToExcludedKeywordDTOs(List<UpdateExcludedKeywordObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordDTO> list = new ArrayList<>( requestObjects.size() );
        for ( UpdateExcludedKeywordObjectRequestModel updateExcludedKeywordObjectRequestModel : requestObjects ) {
            list.add( updateRequestObjectToExcludedKeywordDTO( updateExcludedKeywordObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<ExcludedKeywordEntity> excludedKeywordDTOlistToExcludedKeywordEntityList(List<ExcludedKeywordDTO> dtoList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordEntity> list = new ArrayList<>( dtoList.size() );
        for ( ExcludedKeywordDTO excludedKeywordDTO : dtoList ) {
            list.add( excludedKeywordDTOtoExcludedKeywordEntity( excludedKeywordDTO, userId, filtersId ) );
        }

        return list;
    }

	@Override
	public 	List<ExcludedKeywordEntity> updateExcludedKeywordEntityListFromExcludedKeywordDTOlist(List<ExcludedKeywordDTO> dtoList, List<ExcludedKeywordEntity> entityList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return entityList;
        }

		Map<String, ExcludedKeywordEntity> excludedKeywordEntityMap = createExcludedKeywordEntityMap(entityList);
        entityList.clear();
		for (ExcludedKeywordDTO dto : dtoList) {
	       	if ( !excludedKeywordEntityMap.containsKey(dto.getExcludedKeywordId()) ) {
	       		entityList.add( excludedKeywordDTOtoExcludedKeywordEntity( dto, userId, filtersId ));
        	} else {
        		entityList.add( updateExcludedKeywordEntityFromExcludedKeywordDTO( dto, excludedKeywordEntityMap.get( dto.getExcludedKeywordId()) ) );
        		excludedKeywordEntityMap.remove( dto.getExcludedKeywordId() );
			}
		}
        for ( Map.Entry<String, ExcludedKeywordEntity> entry : excludedKeywordEntityMap.entrySet() ) {
        	entityList.add(entry.getValue());
		}

		return entityList;
	}

    @Override
    public List<ExcludedKeywordDTO> excludedKeywordEntityListToExcludedKeywordDTOlist(List<ExcludedKeywordEntity> entityList) {
        if ( entityList == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordDTO> list = new ArrayList<>( entityList.size() );
        for ( ExcludedKeywordEntity excludedKeywordEntity : entityList ) {
            list.add( excludedKeywordEntityToExcludedKeywordDTO( excludedKeywordEntity ) );
        }

        return list;
    }

    @Override
    public List<ExcludedKeywordObjectResponseModel> excludedKeywordDTOlistToResponseObjects(List<ExcludedKeywordDTO> dtoList) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordObjectResponseModel> list = new ArrayList<>( dtoList.size() );
        for ( ExcludedKeywordDTO excludedKeywordDTO : dtoList ) {
            list.add( excludedKeywordDTOtoResponseObject( excludedKeywordDTO ) );
        }

        return list;
    }

    protected ExcludedKeywordDTO createExcludedKeywordObjectRequestModelToExcludedKeywordDTO(CreateExcludedKeywordObjectRequestModel createExcludedKeywordObjectRequestModel) {
        if ( createExcludedKeywordObjectRequestModel == null ) {
            return null;
        }

        ExcludedKeywordDTO excludedKeywordDTO = new ExcludedKeywordDTO();

        excludedKeywordDTO.setExcludedKeyword( createExcludedKeywordObjectRequestModel.getExcludedKeyword() );

        return excludedKeywordDTO;
    }

    protected ExcludedKeywordDTO updateExcludedKeywordObjectRequestModelToExcludedKeywordDTO(UpdateExcludedKeywordObjectRequestModel updateExcludedKeywordObjectRequestModel, String filtersId) {
        if ( updateExcludedKeywordObjectRequestModel == null ) {
            return null;
        }

        ExcludedKeywordDTO excludedKeywordDTO = new ExcludedKeywordDTO();

        excludedKeywordDTO.setExcludedKeywordId( updateExcludedKeywordObjectRequestModel.getExcludedKeywordId() );
        excludedKeywordDTO.setFiltersId( filtersId );
        excludedKeywordDTO.setExcludedKeyword( updateExcludedKeywordObjectRequestModel.getExcludedKeyword() );

        return excludedKeywordDTO;
    }

	private Map<String, ExcludedKeywordEntity> createExcludedKeywordEntityMap(List<ExcludedKeywordEntity> excludedKeywords) {
		Map<String, ExcludedKeywordEntity> excludedKeywordEntityMap = new HashMap<>();
		for (ExcludedKeywordEntity excludedKeyword : excludedKeywords) {
			excludedKeywordEntityMap.put(excludedKeyword.getExcludedKeywordId(), excludedKeyword);
		}
		return excludedKeywordEntityMap;
	}
}
