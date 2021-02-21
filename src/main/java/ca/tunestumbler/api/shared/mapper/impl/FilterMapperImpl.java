package ca.tunestumbler.api.shared.mapper.impl;

import ca.tunestumbler.api.io.entity.FiltersEntity;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.ExcludedDomainDTO;
import ca.tunestumbler.api.shared.dto.ExcludedKeywordDTO;
import ca.tunestumbler.api.shared.dto.FiltersDTO;
import ca.tunestumbler.api.shared.dto.SelectedDomainDTO;
import ca.tunestumbler.api.shared.dto.SelectedKeywordDTO;
import ca.tunestumbler.api.shared.mapper.ExcludedDomainMapper;
import ca.tunestumbler.api.shared.mapper.ExcludedKeywordMapper;
import ca.tunestumbler.api.shared.mapper.FilterMapper;
import ca.tunestumbler.api.shared.mapper.SelectedDomainMapper;
import ca.tunestumbler.api.shared.mapper.SelectedKeywordMapper;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateExcludedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateExcludedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateFiltersObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateSelectedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateSelectedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateExcludedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateExcludedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateFiltersObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateSelectedDomainObjectRequestModel;
import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateSelectedKeywordObjectRequestModel;
import ca.tunestumbler.api.ui.model.response.filter.object.FiltersObjectResponseModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilterMapperImpl implements FilterMapper {

	private int length = 50;

	@Autowired
	SharedUtils sharedUtils;

	@Autowired
	private ExcludedDomainMapper excludedDomainMapper;

	@Autowired
	private ExcludedKeywordMapper excludedKeywordMapper;

	@Autowired
	private SelectedDomainMapper selectedDomainMapper;

	@Autowired
	private SelectedKeywordMapper selectedKeywordMapper;

	@Override
	public FiltersDTO createRequestObjectToFiltersDTO(CreateFiltersObjectRequestModel requestObject) {
        if ( requestObject == null ) {
            return null;
		}

		FiltersDTO dto = new FiltersDTO();

		dto.setSubreddit(requestObject.getSubreddit());
		dto.setMinScore( requestObject.getMinScore() );
		dto.setAllowNSFWFlag( requestObject.getAllowNSFWFlag() );
		dto.setExcludedDomains( excludedDomainMapper.createRequestObjectsToExcludedDomainDTOs(requestObject.getExcludedDomains() ) );
		dto.setExcludedKeywords( excludedKeywordMapper.createRequestObjectsToExcludedKeywordDTOs(requestObject.getExcludedKeywords() ) );
		dto.setSelectedDomains( selectedDomainMapper.createRequestObjectsToSelectedDomainDTOs(requestObject.getSelectedDomains() ) );
		dto.setSelectedKeywords( selectedKeywordMapper.createRequestObjectsToSelectedKeywordDTOs(requestObject.getSelectedKeywords() ) );

		return dto;
	}

	@Override
	public FiltersDTO updateRequestObjectToFiltersDTO(UpdateFiltersObjectRequestModel requestObject) {
        if ( requestObject == null ) {
            return null;
		}

		FiltersDTO dto = new FiltersDTO();

		dto.setFiltersId(requestObject.getFiltersId());
		dto.setSubreddit(requestObject.getSubreddit());
		dto.setMinScore( requestObject.getMinScore() );
		dto.setAllowNSFWFlag( requestObject.getAllowNSFWFlag() );
		dto.setExcludedDomains( excludedDomainMapper.updateRequestObjectsToExcludedDomainDTOs(requestObject.getExcludedDomains() ) );
		dto.setExcludedKeywords( excludedKeywordMapper.updateRequestObjectsToExcludedKeywordDTOs(requestObject.getExcludedKeywords() ) );
		dto.setSelectedDomains( selectedDomainMapper.updateRequestObjectsToSelectedDomainDTOs(requestObject.getSelectedDomains() ) );
		dto.setSelectedKeywords( selectedKeywordMapper.updateRequestObjectsToSelectedKeywordDTOs(requestObject.getSelectedKeywords() ) );

		return dto;
	}

    @Override
    public FiltersEntity filtersDTOToFiltersEntity(FiltersDTO dto, String userId) {
        if ( dto == null ) {
            return null;
        }

        FiltersEntity filtersEntity = new FiltersEntity();
        
        String filtersId = sharedUtils.generateFiltersId(length);
        filtersEntity.setAllowNSFWFlag( dto.getAllowNSFWFlag() );
        filtersEntity.setExcludedDomains( excludedDomainMapper.excludedDomainDTOlistToExcludedDomainEntityList( dto.getExcludedDomains(), userId, filtersId ) );
        filtersEntity.setExcludedKeywords( excludedKeywordMapper.excludedKeywordDTOlistToExcludedKeywordEntityList( dto.getExcludedKeywords(), userId, filtersId ) );
        filtersEntity.setFiltersId( filtersId );
        filtersEntity.setMinScore( dto.getMinScore() );
        filtersEntity.setSelectedDomains( selectedDomainMapper.selectedDomainDTOlistToSelectedDomainEntityList( dto.getSelectedDomains(), userId, filtersId ) );
        filtersEntity.setSelectedKeywords( selectedKeywordMapper.selectedKeywordDTOlistToSelectedKeywordEntityList( dto.getSelectedKeywords(), userId, filtersId ) );
        filtersEntity.setSubreddit( dto.getSubreddit() );
        filtersEntity.setUserId( userId );

        return filtersEntity;
    }

	@Override
	public FiltersEntity updateFiltersEntityFromFiltersDTO(FiltersDTO dto, FiltersEntity entity) {
        if ( dto == null ) {
            return null;
        }
        
        entity.setFiltersId(dto.getFiltersId());
        entity.setSubreddit(dto.getSubreddit());
        entity.setMinScore(dto.getMinScore());
        entity.setAllowNSFWFlag(dto.getAllowNSFWFlag());
        entity.setExcludedDomains(excludedDomainMapper.updateExcludedDomainEntityListFromExcludedDomainDTOlist(dto.getExcludedDomains(), entity.getExcludedDomains(), entity.getUserId(), entity.getFiltersId() ));
        entity.setExcludedKeywords(excludedKeywordMapper.updateExcludedKeywordEntityListFromExcludedKeywordDTOlist(dto.getExcludedKeywords(), entity.getExcludedKeywords(), entity.getUserId(), entity.getFiltersId() ));
        entity.setSelectedDomains(selectedDomainMapper.updateSelectedDomainEntityListFromSelectedDomainDTOlist(dto.getSelectedDomains(), entity.getSelectedDomains(), entity.getUserId(), entity.getFiltersId() ));
        entity.setSelectedKeywords(selectedKeywordMapper.updateSelectedKeywordEntityListFromSelectedKeywordDTOlist(dto.getSelectedKeywords(), entity.getSelectedKeywords(), entity.getUserId(), entity.getFiltersId() ));

        return entity;
	}

    @Override
    public FiltersDTO filtersEntityToFiltersDTO(FiltersEntity entity) {
        if ( entity == null ) {
            return null;
        }

        FiltersDTO filtersDTO = new FiltersDTO();

        filtersDTO.setAllowNSFWFlag( entity.getAllowNSFWFlag() );
        filtersDTO.setExcludedDomains( excludedDomainMapper.excludedDomainEntityListToExcludedDomainDTOlist( entity.getExcludedDomains() ) );
        filtersDTO.setExcludedKeywords( excludedKeywordMapper.excludedKeywordEntityListToExcludedKeywordDTOlist( entity.getExcludedKeywords() ) );
        filtersDTO.setFiltersId( entity.getFiltersId() );
        filtersDTO.setMinScore( entity.getMinScore() );
        filtersDTO.setSelectedDomains( selectedDomainMapper.selectedDomainEntityListToSelectedDomainDTOlist( entity.getSelectedDomains() ) );
        filtersDTO.setSelectedKeywords( selectedKeywordMapper.selectedKeywordEntityListToSelectedKeywordDTOlist( entity.getSelectedKeywords() ) );
        filtersDTO.setSubreddit( entity.getSubreddit() );

        return filtersDTO;
    }

    @Override
    public FiltersObjectResponseModel filtersDTOtoResponseObject(FiltersDTO dto) {
        if ( dto == null ) {
            return null;
        }

        FiltersObjectResponseModel filtersObjectResponseModel = new FiltersObjectResponseModel();

        filtersObjectResponseModel.setAllowNSFWFlag( dto.getAllowNSFWFlag() );
        filtersObjectResponseModel.setExcludedDomains( excludedDomainMapper.excludedDomainDTOlistToResponseObjects( dto.getExcludedDomains() ) );
        filtersObjectResponseModel.setExcludedKeywords( excludedKeywordMapper.excludedKeywordDTOlistToResponseObjects( dto.getExcludedKeywords() ) );
        filtersObjectResponseModel.setFiltersId( dto.getFiltersId() );
        filtersObjectResponseModel.setMinScore( dto.getMinScore() );
        filtersObjectResponseModel.setSelectedDomains( selectedDomainMapper.selectedDomainDTOlistToResponseObjects( dto.getSelectedDomains() ) );
        filtersObjectResponseModel.setSelectedKeywords( selectedKeywordMapper.selectedKeywordDTOlistToResponseObjects( dto.getSelectedKeywords() ) );
        filtersObjectResponseModel.setSubreddit( dto.getSubreddit() );

        return filtersObjectResponseModel;
    }

    @Override
    public List<FiltersDTO> createRequestObjectsToFiltersDTOs(List<CreateFiltersObjectRequestModel> requestObjects) {
        if ( requestObjects == null || requestObjects.isEmpty() ) {
            return new ArrayList<>();
        }

        List<FiltersDTO> list = new ArrayList<>( requestObjects.size() );
        for ( CreateFiltersObjectRequestModel createRequestObject : requestObjects ) {
            list.add( createRequestObjectToFiltersDTO( createRequestObject ) );
        }

        return list;
    }

    @Override
    public List<FiltersDTO> updateRequestObjectsToFiltersDTOs(List<UpdateFiltersObjectRequestModel> requestObjects) {
        if ( requestObjects == null || requestObjects.isEmpty() ) {
            return new ArrayList<>();
        }

        List<FiltersDTO> list = new ArrayList<>( requestObjects.size() );
        for ( UpdateFiltersObjectRequestModel updateRequestObject : requestObjects ) {
            list.add( updateRequestObjectToFiltersDTO( updateRequestObject ) );
        }

        return list;
    }
    
    @Override
    public List<FiltersEntity> filtersDTOlistToFiltersEntityList(List<FiltersDTO> dtoList, String userId) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<FiltersEntity> list = new ArrayList<>( dtoList.size() );
        for ( FiltersDTO filtersDTO : dtoList ) {
            list.add( filtersDTOToFiltersEntity( filtersDTO, userId ) );
        }

        return list;
    }

	@Override
	public List<FiltersEntity> updateFiltersEntityListFromFiltersDTOlist(List<FiltersDTO> dtoList, List<FiltersEntity> entityList, String userId) {
		if ( dtoList == null ) {
            return new ArrayList<>();
		}

		Map<String, FiltersEntity> filtersEntityMap = createFiltersEntityMap(entityList);
		entityList.clear();
		for (FiltersDTO dto : dtoList) {
			entityList.add( updateFiltersEntityFromFiltersDTO(dto, filtersEntityMap.get(dto.getFiltersId())) );
		}

		return entityList;
	}

    @Override
    public List<FiltersDTO> filtersEntityListToFiltersDTOlist(List<FiltersEntity> entityList) {
        if ( entityList == null ) {
            return new ArrayList<>();
        }

        List<FiltersDTO> list = new ArrayList<>( entityList.size() );
        for ( FiltersEntity filtersEntity : entityList ) {
            list.add( filtersEntityToFiltersDTO( filtersEntity ) );
        }

        return list;
    }

    @Override
    public List<FiltersObjectResponseModel> filtersDTOlistToResponseObjects(List<FiltersDTO> dtoList) {
        if ( dtoList == null ) {
            return new ArrayList<>();
        }

        List<FiltersObjectResponseModel> list = new ArrayList<>( dtoList.size() );
        for ( FiltersDTO filtersDTO : dtoList ) {
            list.add( filtersDTOtoResponseObject( filtersDTO ) );
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

    protected List<ExcludedDomainDTO> createExcludedDomainObjectRequestModelListToExcludedDomainDTOList(List<CreateExcludedDomainObjectRequestModel> list) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainDTO> list1 = new ArrayList<>( list.size() );
        for ( CreateExcludedDomainObjectRequestModel createExcludedDomainObjectRequestModel : list ) {
            list1.add( createExcludedDomainObjectRequestModelToExcludedDomainDTO( createExcludedDomainObjectRequestModel ) );
        }

        return list1;
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

    protected List<ExcludedDomainDTO> updateExcludedDomainObjectRequestModelListToExcludedDomainDTOList(List<UpdateExcludedDomainObjectRequestModel> list, String filtersId) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<ExcludedDomainDTO> list1 = new ArrayList<>( list.size() );
        for ( UpdateExcludedDomainObjectRequestModel updateExcludedDomainObjectRequestModel : list ) {
            list1.add( updateExcludedDomainObjectRequestModelToExcludedDomainDTO( updateExcludedDomainObjectRequestModel, filtersId ) );
        }

        return list1;
    }

    protected ExcludedKeywordDTO createExcludedKeywordObjectRequestModelToExcludedKeywordDTO(CreateExcludedKeywordObjectRequestModel createExcludedKeywordObjectRequestModel) {
        if ( createExcludedKeywordObjectRequestModel == null ) {
            return null;
        }

        ExcludedKeywordDTO excludedKeywordDTO = new ExcludedKeywordDTO();

        excludedKeywordDTO.setExcludedKeyword( createExcludedKeywordObjectRequestModel.getExcludedKeyword() );

        return excludedKeywordDTO;
    }

    protected List<ExcludedKeywordDTO> createExcludedKeywordObjectRequestModelListToExcludedKeywordDTOList(List<CreateExcludedKeywordObjectRequestModel> list) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordDTO> list1 = new ArrayList<>( list.size() );
        for ( CreateExcludedKeywordObjectRequestModel createExcludedKeywordObjectRequestModel : list ) {
            list1.add( createExcludedKeywordObjectRequestModelToExcludedKeywordDTO( createExcludedKeywordObjectRequestModel ) );
        }

        return list1;
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

    protected List<ExcludedKeywordDTO> updateExcludedKeywordObjectRequestModelListToExcludedKeywordDTOList(List<UpdateExcludedKeywordObjectRequestModel> list, String filtersId) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<ExcludedKeywordDTO> list1 = new ArrayList<>( list.size() );
        for ( UpdateExcludedKeywordObjectRequestModel updateExcludedKeywordObjectRequestModel : list ) {
            list1.add( updateExcludedKeywordObjectRequestModelToExcludedKeywordDTO( updateExcludedKeywordObjectRequestModel, filtersId ) );
        }

        return list1;
    }

    protected SelectedDomainDTO createSelectedDomainObjectRequestModelToSelectedDomainDTO(CreateSelectedDomainObjectRequestModel createSelectedDomainObjectRequestModel) {
        if ( createSelectedDomainObjectRequestModel == null ) {
            return null;
        }

        SelectedDomainDTO selectedDomainDTO = new SelectedDomainDTO();

        selectedDomainDTO.setSelectedDomain( createSelectedDomainObjectRequestModel.getSelectedDomain() );

        return selectedDomainDTO;
    }

    protected List<SelectedDomainDTO> createSelectedDomainObjectRequestModelListToSelectedDomainDTOList(List<CreateSelectedDomainObjectRequestModel> list) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainDTO> list1 = new ArrayList<>( list.size() );
        for ( CreateSelectedDomainObjectRequestModel createSelectedDomainObjectRequestModel : list ) {
            list1.add( createSelectedDomainObjectRequestModelToSelectedDomainDTO( createSelectedDomainObjectRequestModel ) );
		}

        return list1;
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

    protected List<SelectedDomainDTO> updateSelectedDomainObjectRequestModelListToSelectedDomainDTOList(List<UpdateSelectedDomainObjectRequestModel> list, String filtersId) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<SelectedDomainDTO> list1 = new ArrayList<>( list.size() );
        for ( UpdateSelectedDomainObjectRequestModel updateSelectedDomainObjectRequestModel : list ) {
            list1.add( updateSelectedDomainObjectRequestModelToSelectedDomainDTO( updateSelectedDomainObjectRequestModel, filtersId ) );
        }

        return list1;
    }

    protected SelectedKeywordDTO createSelectedKeywordObjectRequestModelToSelectedKeywordDTO(CreateSelectedKeywordObjectRequestModel createSelectedKeywordObjectRequestModel) {
        if ( createSelectedKeywordObjectRequestModel == null ) {
            return null;
        }

        SelectedKeywordDTO selectedKeywordDTO = new SelectedKeywordDTO();

        selectedKeywordDTO.setSelectedKeyword( createSelectedKeywordObjectRequestModel.getSelectedKeyword() );

        return selectedKeywordDTO;
    }

    protected List<SelectedKeywordDTO> createSelectedKeywordObjectRequestModelListToSelectedKeywordDTOList(List<CreateSelectedKeywordObjectRequestModel> list) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordDTO> list1 = new ArrayList<>( list.size() );
        for ( CreateSelectedKeywordObjectRequestModel createSelectedKeywordObjectRequestModel : list ) {
            list1.add( createSelectedKeywordObjectRequestModelToSelectedKeywordDTO( createSelectedKeywordObjectRequestModel ) );
        }

        return list1;
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

    protected List<SelectedKeywordDTO> updateSelectedKeywordObjectRequestModelListToSelectedKeywordDTOList(List<UpdateSelectedKeywordObjectRequestModel> list, String filtersId) {
        if ( list == null ) {
            return new ArrayList<>();
        }

        List<SelectedKeywordDTO> list1 = new ArrayList<>( list.size() );
        for ( UpdateSelectedKeywordObjectRequestModel updateSelectedKeywordObjectRequestModel : list ) {
            list1.add( updateSelectedKeywordObjectRequestModelToSelectedKeywordDTO( updateSelectedKeywordObjectRequestModel, filtersId ) );
        }

        return list1;
    }

    protected FiltersDTO createFiltersObjectRequestModelToFiltersDTO(CreateFiltersObjectRequestModel createFiltersObjectRequestModel) {
        if ( createFiltersObjectRequestModel == null ) {
            return null;
        }

        FiltersDTO filtersDTO = new FiltersDTO();

        filtersDTO.setAllowNSFWFlag( createFiltersObjectRequestModel.getAllowNSFWFlag() );
        filtersDTO.setExcludedDomains( createExcludedDomainObjectRequestModelListToExcludedDomainDTOList( createFiltersObjectRequestModel.getExcludedDomains() ) );
        filtersDTO.setExcludedKeywords( createExcludedKeywordObjectRequestModelListToExcludedKeywordDTOList( createFiltersObjectRequestModel.getExcludedKeywords() ) );
        filtersDTO.setMinScore( createFiltersObjectRequestModel.getMinScore() );
        filtersDTO.setSelectedDomains( createSelectedDomainObjectRequestModelListToSelectedDomainDTOList( createFiltersObjectRequestModel.getSelectedDomains() ) );
        filtersDTO.setSelectedKeywords( createSelectedKeywordObjectRequestModelListToSelectedKeywordDTOList( createFiltersObjectRequestModel.getSelectedKeywords() ) );
        filtersDTO.setSubreddit( createFiltersObjectRequestModel.getSubreddit() );

        return filtersDTO;
    }

    protected FiltersDTO updateFiltersObjectRequestModelToFiltersDTO(UpdateFiltersObjectRequestModel updateFiltersObjectRequestModel) {
        if ( updateFiltersObjectRequestModel == null ) {
            return null;
        }

        FiltersDTO filtersDTO = new FiltersDTO();

        filtersDTO.setAllowNSFWFlag( updateFiltersObjectRequestModel.getAllowNSFWFlag() );
        filtersDTO.setExcludedDomains( updateExcludedDomainObjectRequestModelListToExcludedDomainDTOList( updateFiltersObjectRequestModel.getExcludedDomains(), updateFiltersObjectRequestModel.getFiltersId() ) );
        filtersDTO.setExcludedKeywords( updateExcludedKeywordObjectRequestModelListToExcludedKeywordDTOList( updateFiltersObjectRequestModel.getExcludedKeywords(), updateFiltersObjectRequestModel.getFiltersId() ) );
        filtersDTO.setFiltersId( updateFiltersObjectRequestModel.getFiltersId() );
        filtersDTO.setMinScore( updateFiltersObjectRequestModel.getMinScore() );
        filtersDTO.setSelectedDomains( updateSelectedDomainObjectRequestModelListToSelectedDomainDTOList( updateFiltersObjectRequestModel.getSelectedDomains(), updateFiltersObjectRequestModel.getFiltersId() ) );
        filtersDTO.setSelectedKeywords( updateSelectedKeywordObjectRequestModelListToSelectedKeywordDTOList( updateFiltersObjectRequestModel.getSelectedKeywords(),  updateFiltersObjectRequestModel.getFiltersId() ) );
        filtersDTO.setSubreddit( updateFiltersObjectRequestModel.getSubreddit() );

        return filtersDTO;
    }

    private Map<String, FiltersEntity> createFiltersEntityMap(List<FiltersEntity> filters) {
		Map<String, FiltersEntity> filtersEntityMap = new HashMap<>();
		for (FiltersEntity filter : filters) {
			filtersEntityMap.put(filter.getFiltersId(), filter);
		}
		return filtersEntityMap;
	}
}
