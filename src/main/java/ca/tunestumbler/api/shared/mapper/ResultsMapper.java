package ca.tunestumbler.api.shared.mapper;

import java.util.List;

import ca.tunestumbler.api.io.entity.ResultsEntity;
import ca.tunestumbler.api.shared.dto.NextResultsRequestDTO;
import ca.tunestumbler.api.shared.dto.PlaylistDTO;
import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.shared.dto.ResultsResponseDTO;
import ca.tunestumbler.api.ui.model.request.NextResultsRequestModel;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;
import ca.tunestumbler.api.ui.model.response.playlist.PlaylistModel;
import ca.tunestumbler.api.ui.model.response.playlist.PlaylistResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;

public interface ResultsMapper {

	ResultsDTO resultDataToFiltersDTO(ResultsDataChildrenDataModel resultData);

	NextResultsRequestDTO nextResultsRequestToDTO(NextResultsRequestModel request);

	ResultsEntity resultsDTOtoResultsEntity(ResultsDTO dto, String userId, long startId);

	ResultsDTO resultsEntityToResultsDTO(ResultsEntity entity);

	ResultsObjectResponseModel resultsDTOtoResultsResponseObject(ResultsDTO dto);

	PlaylistDTO createPlaylistDTO(String subreddits, String url);

	PlaylistModel playlistDTOtoPlaylistModel(PlaylistDTO dto);

	List<ResultsDTO> resultsDataChildrenListToResultsDTOlist(List<ResultsDataChildrenModel> resultDataList);

	List<ResultsEntity> resultsDTOlistToResultsEntityList(List<ResultsDTO> dtoList, String userId, long startId);

	List<ResultsDTO> resultsEntityListToResultsDTOlist(List<ResultsEntity> entityList);

	List<ResultsObjectResponseModel> resultsDTOlistToResultsResponseObjectList(List<ResultsDTO> dtoList);

	ResultsResponseDTO resultsDTOlistToResultsResponseDTO(List<ResultsDTO> dtoList, String afterId, String uri);

	ResultsResponseModel resultsReponseDTOtoResultsResponseModel(ResultsResponseDTO dto);

	List<PlaylistModel> playlistDTOlistToPlaylistModelList(List<PlaylistDTO> dtoList);

	PlaylistResponseModel playlistDTOlistToPlaylistResponseModel(List<PlaylistDTO> dtoList);

}
