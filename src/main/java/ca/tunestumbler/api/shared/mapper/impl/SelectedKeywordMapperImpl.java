package ca.tunestumbler.api.shared.mapper.impl;

import ca.tunestumbler.api.io.entity.SelectedKeywordEntity;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SelectedKeywordDTO;
import ca.tunestumbler.api.shared.mapper.SelectedKeywordMapper;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateSelectedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateSelectedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.SelectedKeywordObjectResponseModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectedKeywordMapperImpl implements SelectedKeywordMapper {

	private int length = 50;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public SelectedKeywordDTO createRequestObjectToSelectedKeywordDTO(CreateSelectedKeywordObjectRequestModel requestObject) {
		SelectedKeywordDTO dto = new SelectedKeywordDTO();
		dto.setSelectedKeyword(requestObject.getSelectedKeyword());

		return dto;
	}

	@Override
	public SelectedKeywordDTO updateRequestObjectToSelectedKeywordDTO(UpdateSelectedKeywordObjectRequestModel requestObject) {
		SelectedKeywordDTO dto = new SelectedKeywordDTO();
		dto.setSelectedKeywordId(requestObject.getSelectedKeywordId());
		dto.setSelectedKeyword(requestObject.getSelectedKeyword());

		return dto;
	}

    @Override
    public SelectedKeywordEntity selectedKeywordDTOtoSelectedKeywordEntity(SelectedKeywordDTO dto, String userId, String filtersId) {
        if ( dto == null ) {
            return null;
        }

        SelectedKeywordEntity selectedKeywordEntity = new SelectedKeywordEntity();

        selectedKeywordEntity.setSelectedKeywordId( sharedUtils.generateFiltersId(length) );
        selectedKeywordEntity.setUserId( userId );
        selectedKeywordEntity.setFiltersId( filtersId );;
        selectedKeywordEntity.setSelectedKeyword( dto.getSelectedKeyword() );

        return selectedKeywordEntity;
    }

	@Override
	public SelectedKeywordEntity updateSelectedKeywordEntityFromSelectedKeywordDTO(SelectedKeywordDTO dto, SelectedKeywordEntity entity) {
		if (dto == null) {
			return null;
		}

		entity.setSelectedKeyword(dto.getSelectedKeyword());

		return entity;
	}	

    @Override
    public SelectedKeywordDTO selectedKeywordEntityToSelectedKeywordDTO(SelectedKeywordEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SelectedKeywordDTO selectedKeywordDTO = new SelectedKeywordDTO();

        selectedKeywordDTO.setSelectedKeywordId( entity.getSelectedKeywordId() );
        selectedKeywordDTO.setFiltersId( entity.getFiltersId() );
        selectedKeywordDTO.setSelectedKeyword( entity.getSelectedKeyword() );

        return selectedKeywordDTO;
    }

    @Override
    public SelectedKeywordObjectResponseModel selectedKeywordDTOtoResponseObject(SelectedKeywordDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SelectedKeywordObjectResponseModel selectedKeywordObjectResponseModel = new SelectedKeywordObjectResponseModel();

        selectedKeywordObjectResponseModel.setSelectedKeywordId( dto.getSelectedKeywordId() );
        selectedKeywordObjectResponseModel.setSelectedKeyword( dto.getSelectedKeyword() );

        return selectedKeywordObjectResponseModel;
    }

    @Override
    public List<SelectedKeywordDTO> createRequestObjectsToSelectedKeywordDTOs(List<CreateSelectedKeywordObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordDTO> list = new ArrayList<>( requestObjects.size() );
        for ( CreateSelectedKeywordObjectRequestModel createSelectedKeywordObjectRequestModel : requestObjects ) {
            list.add( createRequestObjectToSelectedKeywordDTO( createSelectedKeywordObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<SelectedKeywordDTO> updateRequestObjectsToSelectedKeywordDTOs(List<UpdateSelectedKeywordObjectRequestModel> requestObjects) {
        if ( requestObjects == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordDTO> list = new ArrayList<>( requestObjects.size() );
        for ( UpdateSelectedKeywordObjectRequestModel updateSelectedKeywordObjectRequestModel : requestObjects ) {
            list.add( updateRequestObjectToSelectedKeywordDTO( updateSelectedKeywordObjectRequestModel ) );
        }

        return list;
    }

    @Override
    public List<SelectedKeywordEntity> selectedKeywordDTOlistToSelectedKeywordEntityList(List<SelectedKeywordDTO> dtoList, String user, String filtersId) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordEntity> list = new ArrayList<>( dtoList.size() );
        for ( SelectedKeywordDTO selectedKeywordDTO : dtoList ) {
            list.add( selectedKeywordDTOtoSelectedKeywordEntity( selectedKeywordDTO, user, filtersId ) );
        }

        return list;
    }

	@Override
	public List<SelectedKeywordEntity> updateSelectedKeywordEntityListFromSelectedKeywordDTOlist(List<SelectedKeywordDTO> dtoList, List<SelectedKeywordEntity> entityList, String userId, String filtersId) {
        if ( dtoList == null ) {
            return entityList;
        }

        Map<String, SelectedKeywordEntity> selectedKeywordEntityMap = createSelectedKeywordEntityMap(entityList);
        entityList.clear();
        for ( SelectedKeywordDTO dto : dtoList ) {
        	if ( !selectedKeywordEntityMap.containsKey(dto.getSelectedKeywordId()) ) {
        		entityList.add( selectedKeywordDTOtoSelectedKeywordEntity( dto, userId, filtersId ));
        	} else {
        		entityList.add( updateSelectedKeywordEntityFromSelectedKeywordDTO( dto, selectedKeywordEntityMap.get(dto.getSelectedKeywordId()) ) );
        		selectedKeywordEntityMap.remove( dto.getSelectedKeywordId() );
			}
		}
        for ( Map.Entry<String, SelectedKeywordEntity> entry : selectedKeywordEntityMap.entrySet() ) {
        	entityList.add(entry.getValue());
		}

		return entityList;
	}

    @Override
    public List<SelectedKeywordDTO> selectedKeywordEntityListToSelectedKeywordDTOlist(List<SelectedKeywordEntity> entityList) {
        if ( entityList == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordDTO> list = new ArrayList<>( entityList.size() );
        for ( SelectedKeywordEntity selectedKeywordEntity : entityList ) {
            list.add( selectedKeywordEntityToSelectedKeywordDTO( selectedKeywordEntity ) );
        }

        return list;
    }

    @Override
    public List<SelectedKeywordObjectResponseModel> selectedKeywordDTOlistToResponseObjects(List<SelectedKeywordDTO> dtoList) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordObjectResponseModel> list = new ArrayList<>( dtoList.size() );
        for ( SelectedKeywordDTO selectedKeywordDTO : dtoList ) {
            list.add( selectedKeywordDTOtoResponseObject( selectedKeywordDTO ) );
        }

        return list;
    }

    protected SelectedKeywordDTO createselectedKeywordObjectRequestModelToSelectedKeywordDTO(CreateSelectedKeywordObjectRequestModel createSelectedKeywordObjectRequestModel) {
        if ( createSelectedKeywordObjectRequestModel == null ) {
            return null;
        }

        SelectedKeywordDTO selectedKeywordDTO = new SelectedKeywordDTO();

        selectedKeywordDTO.setSelectedKeyword( createSelectedKeywordObjectRequestModel.getSelectedKeyword() );

        return selectedKeywordDTO;
    }

    protected SelectedKeywordDTO updateSelectedKeywordObjectRequestModelToSelectedKeywordDTO(UpdateSelectedKeywordObjectRequestModel updateSelectedKeywordObjectRequestModel, String filtersId) {
        if ( updateSelectedKeywordObjectRequestModel == null ) {
            return null;
        }

        SelectedKeywordDTO selectedKeywordDTO = new SelectedKeywordDTO();

        selectedKeywordDTO.setSelectedKeywordId( updateSelectedKeywordObjectRequestModel.getSelectedKeywordId() );
        selectedKeywordDTO.setFiltersId( filtersId );
        selectedKeywordDTO.setSelectedKeyword( updateSelectedKeywordObjectRequestModel.getSelectedKeyword() );

        return selectedKeywordDTO;
    }

	private Map<String, SelectedKeywordEntity> createSelectedKeywordEntityMap(List<SelectedKeywordEntity> selectedKeywords) {
		Map<String, SelectedKeywordEntity> selectedKeywordEntityMap = new HashMap<>();
		for (SelectedKeywordEntity selectedKeyword : selectedKeywords) {
			selectedKeywordEntityMap.put(selectedKeyword.getSelectedKeywordId(), selectedKeyword);
		}
		return selectedKeywordEntityMap;
	}
}
