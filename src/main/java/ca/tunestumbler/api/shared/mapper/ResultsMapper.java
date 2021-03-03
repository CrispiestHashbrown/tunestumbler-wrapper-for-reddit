package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.ResultsEntity;
import ca.tunestumbler.api.shared.dto.NextResultsRequestDTO;
import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.ui.model.request.NextResultsRequestModel;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;

public interface ResultsMapper {

	ResultsDTO resultDataToFiltersDTO(ResultsDataChildrenDataModel resultData);

	NextResultsRequestDTO nextResultsRequestToDTO(NextResultsRequestModel request);

	ResultsEntity resultsDTOtoResultsEntity(ResultsDTO dto, String userId, long startId);

	ResultsDTO resultsEntityToResultsDTO(ResultsEntity entity);

	ResultsObjectResponseModel resultsDTOtoResultsResponseObject(ResultsDTO dto);

	ResultsResponseModel buildResponseModel(List<ResultsObjectResponseModel> responseObjects, String afterId, String uri);

	List<ResultsDTO> resultsDataChildrenListToResultsDTOlist(List<ResultsDataChildrenModel> resultDataList);

	List<ResultsEntity> resultsDTOlistToResultsEntityList(List<ResultsDTO> dtoList, String userId, long startId);

	List<ResultsDTO> resultsEntityListToResultsDTOlist(List<ResultsEntity> entityList);

	List<ResultsObjectResponseModel> resultsDTOlistToResultsResponseObjectList(List<ResultsDTO> dtoList);

}
