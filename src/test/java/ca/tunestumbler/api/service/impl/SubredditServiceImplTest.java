package ca.tunestumbler.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.tunestumbler.api.io.entity.SubredditEntity;
import ca.tunestumbler.api.io.repositories.SubredditRepository;
import ca.tunestumbler.api.service.impl.helpers.SubredditHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

public class SubredditServiceImplTest {

	@InjectMocks
	SubredditServiceImpl subredditService;
	
	@Mock
	SubredditRepository subredditRepository;

	@Mock
	SubredditHelpers subredditHelpers;
	
	@Mock
	SharedUtils sharedUtils;
	
	String userId = "userId";
	String encryptedPassword = "12324";
	String randomDate = "July 1, 2020 00:00:00";
	UserDTO userDTO;
	Long startId = 1L;
	String afterId = "afterId";
	String beforeId = "beforeId";
	List<SubredditEntity> subredditEntities;
	SubredditEntity subredditEntity;
	SubredditFetchResponseModel response;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDTO = new UserDTO();
		userDTO.setUserId(userId);
		userDTO.setEmail("test@test");
		userDTO.setEncryptedPassword(encryptedPassword);
		userDTO.setToken("123");
		userDTO.setRefreshToken("123");
		userDTO.setTokenLifetime("3600");
		userDTO.setLastModified(randomDate);
		
		subredditEntities = new ArrayList<>();
		subredditEntity = new SubredditEntity();
		subredditEntity.setSubredditId("subredditId");
		subredditEntity.setAfterId("afterId");
		subredditEntity.setBeforeId("beforeId");
		subredditEntity.setIsSubscribed(true);
		subredditEntity.setLastModified(randomDate);
		subredditEntity.setStartId(startId);
		subredditEntity.setSubreddit("subreddit");
		subredditEntity.setUserId(userId);
		subredditEntities.add(subredditEntity);
		

		SubredditDataChildrenDataModel dataProperties = new SubredditDataChildrenDataModel();
		dataProperties.setDisplay_name("testSubreddit");
		
		SubredditDataChildrenModel data = new SubredditDataChildrenModel();
		data.setData(dataProperties);
		
		List<SubredditDataChildrenModel> children = new ArrayList<>();
		children.add(data);
		
		SubredditDataModel responseData = new SubredditDataModel();
		responseData.setAfter(afterId);
		responseData.setBefore(beforeId);
		responseData.setChildren(children);
		
		response = new SubredditFetchResponseModel();
		response.setData(responseData);
	}

	@Test
	void testFetchSubreddits() {
		when(subredditRepository.findMaxIdByUserId(anyString())).thenReturn(startId);
		when(subredditRepository.findMaxId()).thenReturn(startId);
		when(sharedUtils.setStartId(startId, startId)).thenReturn(startId);
		when(subredditHelpers.sendGetSubredditRequest(ArgumentMatchers.any(UserDTO.class))).thenReturn(response);
		when(subredditRepository.saveAll(ArgumentMatchers.<SubredditEntity>anyList())).thenReturn(subredditEntities);
		
		UserDTO userDTO = new UserDTO();
		List<SubredditDTO> subreddits = subredditService.fetchSubreddits(userDTO); 
		
		assertEquals(subredditEntities.get(0).getSubredditId(), subreddits.get(0).getSubredditId());
		assertEquals(subredditEntities.get(0).getAfterId(), subreddits.get(0).getAfterId());
		assertEquals(subredditEntities.get(0).getBeforeId(), subreddits.get(0).getBeforeId());
		assertEquals(subredditEntities.get(0).getIsSubscribed(), subreddits.get(0).getIsSubscribed());
		assertEquals(subredditEntities.get(0).getLastModified(), subreddits.get(0).getLastModified());
		assertEquals(subredditEntities.get(0).getStartId(), subreddits.get(0).getStartId());
		assertEquals(subredditEntities.get(0).getSubreddit(), subreddits.get(0).getSubreddit());
		assertEquals(subredditEntities.get(0).getUserId(), subreddits.get(0).getUserId());
	}
}
