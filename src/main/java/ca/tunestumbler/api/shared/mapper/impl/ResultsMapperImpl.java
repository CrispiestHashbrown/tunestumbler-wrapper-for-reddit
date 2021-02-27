package ca.tunestumbler.api.shared.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.tunestumbler.api.io.entity.ResultsEntity;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.NextResultsRequestDTO;
import ca.tunestumbler.api.shared.dto.ResultsDTO;
import ca.tunestumbler.api.shared.mapper.ResultsMapper;
import ca.tunestumbler.api.ui.model.request.NextResultsRequestModel;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.results.ResultsObjectResponseModel;

@Component
public class ResultsMapperImpl implements ResultsMapper {

	private int length = 50;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public ResultsDTO resultDataToFiltersDTO(ResultsDataChildrenDataModel resultData) {
		if (resultData == null) {
			return null;
		}

		ResultsDTO dto = new ResultsDTO();

		dto.setSubreddit(resultData.getSubreddit());
		dto.setTitle(resultData.getTitle());
		dto.setScore(resultData.getScore());
		dto.setCreated(resultData.getCreated());
		dto.setCreatedUtc(resultData.getCreated_utc());
		dto.setDomain(resultData.getDomain());
		dto.setIsNsfw(resultData.getOver_18());
		dto.setIsSpoiler(resultData.getSpoiler());
		dto.setComments(resultData.getNum_comments());
		dto.setPermalink(resultData.getPermalink());
		dto.setIsStickied(resultData.getStickied());
		dto.setUrl(resultData.getUrl());

		return dto;
	}

	@Override
	public NextResultsRequestDTO nextResultsRequestToDTO(NextResultsRequestModel request) {
		if (request == null) {
			return null;
		}

		NextResultsRequestDTO dto = new NextResultsRequestDTO();

		dto.setAfterId(request.getAfterId());
		dto.setNextUri(request.getNextUri());

		return dto;
	}

	@Override
	public ResultsEntity resultsDTOtoResultsEntity(ResultsDTO dto, String userId, long startId) {
		if (dto == null) {
			return null;
		}

		ResultsEntity entity = new ResultsEntity();

		entity.setResultsId(sharedUtils.generateResultsId(length));
		entity.setUserId(userId);
		entity.setSubreddit(dto.getSubreddit());
		entity.setTitle(dto.getTitle());
		entity.setScore(dto.getScore());
		entity.setCreated(dto.getCreated());
		entity.setCreatedUtc(dto.getCreatedUtc());
		entity.setDomain(dto.getDomain());
		entity.setIsNsfw(dto.getIsNsfw());
		entity.setIsSpoiler(dto.getIsSpoiler());
		entity.setComments(dto.getComments());
		entity.setPermalink(dto.getPermalink());
		entity.setIsStickied(dto.getIsStickied());
		entity.setUrl(dto.getUrl());
		entity.setStartId(startId);

		return entity;
	}

	@Override
	public ResultsDTO resultsEntityToResultsDTO(ResultsEntity entity) {
		if (entity == null) {
			return null;
		}

		ResultsDTO dto = new ResultsDTO();

		dto.setResultsId(entity.getResultsId());
		dto.setSubreddit(entity.getSubreddit());
		dto.setTitle(entity.getTitle());
		dto.setScore(entity.getScore());
		dto.setCreated(entity.getCreated());
		dto.setCreatedUtc(entity.getCreatedUtc());
		dto.setDomain(entity.getDomain());
		dto.setIsNsfw(entity.getIsNsfw());
		dto.setIsSpoiler(entity.getIsSpoiler());
		dto.setComments(entity.getComments());
		dto.setPermalink(entity.getPermalink());
		dto.setIsStickied(entity.getIsStickied());
		dto.setUrl(entity.getUrl());

		return dto;
	}

	@Override
	public ResultsObjectResponseModel resultsDTOtoResultsResponseObject(ResultsDTO dto) {
		if (dto == null) {
			return null;
		}

		ResultsObjectResponseModel resultsObjectResponseModel = new ResultsObjectResponseModel();

		resultsObjectResponseModel.setResultsId(dto.getResultsId());
		resultsObjectResponseModel.setSubreddit(dto.getSubreddit());
		resultsObjectResponseModel.setTitle(dto.getTitle());
		resultsObjectResponseModel.setScore(dto.getScore());
		resultsObjectResponseModel.setCreated(dto.getCreated());
		resultsObjectResponseModel.setCreatedUtc(dto.getCreatedUtc());
		resultsObjectResponseModel.setDomain(dto.getDomain());
		resultsObjectResponseModel.setIsNsfw(dto.getIsNsfw());
		resultsObjectResponseModel.setIsSpoiler(dto.getIsSpoiler());
		resultsObjectResponseModel.setComments(dto.getComments());
		resultsObjectResponseModel.setPermalink(dto.getPermalink());
		resultsObjectResponseModel.setIsStickied(dto.getIsStickied());
		resultsObjectResponseModel.setUrl(dto.getUrl());

		return resultsObjectResponseModel;
	}

	@Override
	public ResultsResponseModel buildResponseModel(List<ResultsObjectResponseModel> responseObjects, String afterId, String uri) {
		if (responseObjects == null || responseObjects.isEmpty()) {
			return new ResultsResponseModel();
		}

		ResultsResponseModel responseModel = new ResultsResponseModel();
		responseModel.setResults(responseObjects);
		responseModel.setAfterId(afterId);
		responseModel.setNextUri(uri);

		return responseModel;
	}

	@Override
	public List<ResultsDTO> resultsDataChildrenListToResultsDTOlist(List<ResultsDataChildrenModel> resultDataList) {
		if (resultDataList == null || resultDataList.isEmpty()) {
			return new ArrayList<>();
		}
		List<ResultsDTO> list = new ArrayList<>(resultDataList.size());
		for (ResultsDataChildrenModel resultData : resultDataList) {
			list.add(resultDataToFiltersDTO(resultData.getData()));
		}

		return list;
	}

	@Override
	public List<ResultsEntity> resultsDTOlistToResultsEntityList(List<ResultsDTO> dtoList, String userId, long startId) {
		if (dtoList == null || dtoList.isEmpty()) {
			return new ArrayList<>();
		}
		List<ResultsEntity> list = new ArrayList<>(dtoList.size());
		for (ResultsDTO dto : dtoList) {
			list.add(resultsDTOtoResultsEntity(dto, userId, startId));
		}

		return list;
	}

	@Override
	public List<ResultsDTO> resultsEntityListToResultsDTOlist(List<ResultsEntity> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return new ArrayList<>();
		}
		List<ResultsDTO> list = new ArrayList<>(entityList.size());
		for (ResultsEntity entity : entityList) {
			list.add(resultsEntityToResultsDTO(entity));
		}

		return list;
	}

	@Override
	public List<ResultsObjectResponseModel> resultsDTOlistToResultsResponseObjectList(List<ResultsDTO> dtoList) {
		if (dtoList == null || dtoList.isEmpty()) {
			return new ArrayList<>();
		}
		List<ResultsObjectResponseModel> list = new ArrayList<>(dtoList.size());
		for (ResultsDTO dto : dtoList) {
			list.add(resultsDTOtoResultsResponseObject(dto));
		}

		return list;
	}

}
