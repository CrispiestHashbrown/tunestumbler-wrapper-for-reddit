package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.tunestumbler.api.io.entity.MultiredditEntity;
import ca.tunestumbler.api.io.repositories.MultiredditRepository;
import ca.tunestumbler.api.service.impl.helpers.MultiredditHelpers;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditDataModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditDataSubredditModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditFetchResponseModel;

public class MultiredditServiceImplTest {

	@InjectMocks
	MultiredditServiceImpl multiredditService;

	@Mock
	MultiredditRepository multiredditRepository;

	@Mock
	MultiredditHelpers multiredditHelpers;

	@Mock
	SharedUtils sharedUtils;
	
	String userId = "userId";
	String randomDate = "July 1, 2020 00:00:00";
	UserDTO userDTO;
	Long startId = 1L;
	List<MultiredditEntity> multiredditEntities;
	MultiredditEntity multiredditEntity;
	MultiredditFetchResponseModel[] response;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDTO = new UserDTO();
		userDTO.setUserId(userId);
		userDTO.setEmail("test@test");
		userDTO.setEncryptedPassword("12324");
		userDTO.setToken("123");
		userDTO.setRefreshToken("123");
		userDTO.setTokenLifetime("3600");
		userDTO.setLastModified(randomDate);

		multiredditEntity = new MultiredditEntity();
		multiredditEntity.setMultiredditId("multiredditId");
		multiredditEntity.setIsCurated(true);
		multiredditEntity.setLastModified(randomDate);
		multiredditEntity.setStartId(startId);
		multiredditEntity.setMultireddit("multireddit");
		multiredditEntity.setSubreddit("subreddit");
		multiredditEntity.setUserId(userId);
		multiredditEntities = new ArrayList<>();
		multiredditEntities.add(multiredditEntity);
		
		MultiredditDataSubredditModel subreddits = new MultiredditDataSubredditModel();
		subreddits.setName("someSubreddit");
		List<MultiredditDataSubredditModel> subredditsList = new ArrayList<>();		
		subredditsList.add(subreddits);
		
		MultiredditDataModel data = new MultiredditDataModel();
		data.setName("multiredditName");
		data.setSubreddits(subredditsList);
		
		MultiredditFetchResponseModel multiredditData = new MultiredditFetchResponseModel();
		multiredditData.setData(data);
		
		response = new MultiredditFetchResponseModel[]{multiredditData};
	}

	@Test
	void testFetchMultireddits() {
		when(multiredditRepository.findMaxIdByUserId(anyString())).thenReturn(startId);
		when(multiredditRepository.findMaxId()).thenReturn(startId);
		when(sharedUtils.setStartId(startId, startId)).thenReturn(startId);
		when(multiredditHelpers.sendGetMultiredditRequest(ArgumentMatchers.any(UserDTO.class))).thenReturn(response);
		when(multiredditRepository.saveAll(ArgumentMatchers.<MultiredditEntity>anyList())).thenReturn(multiredditEntities);

		UserDTO userDTO = new UserDTO();
		List<MultiredditDTO> multireddits = multiredditService.fetchMultireddits(userDTO);
		
		assertEquals(multiredditEntities.get(0).getMultiredditId(), multireddits.get(0).getMultiredditId());
		assertEquals(multiredditEntities.get(0).getIsCurated(), multireddits.get(0).getIsCurated());
		assertEquals(multiredditEntities.get(0).getLastModified(), multireddits.get(0).getLastModified());
		assertEquals(multiredditEntities.get(0).getStartId(), multireddits.get(0).getStartId());
		assertEquals(multiredditEntities.get(0).getMultireddit(), multireddits.get(0).getMultireddit());
		assertEquals(multiredditEntities.get(0).getSubreddit(), multireddits.get(0).getSubreddit());
		assertEquals(multiredditEntities.get(0).getUserId(), multireddits.get(0).getUserId());
	}
	
}
