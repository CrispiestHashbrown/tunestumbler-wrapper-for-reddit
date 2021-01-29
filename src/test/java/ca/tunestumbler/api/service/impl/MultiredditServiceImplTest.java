package ca.tunestumbler.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
	MultiredditHelpers multiredditHelpers;

	@Mock
	SharedUtils sharedUtils;
	
	UserDTO userDTO;
	List<MultiredditDTO> multiredditDTOs;
	MultiredditDTO multiredditDTO;
	MultiredditFetchResponseModel[] response;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDTO = new UserDTO();
		userDTO.setUserId("userId");
		userDTO.setEmail("test@test");
		userDTO.setEncryptedPassword("12324");
		userDTO.setToken("123");
		userDTO.setRefreshToken("123");
		userDTO.setTokenLifetime("3600");
		userDTO.setLastModified("July 1, 2020 00:00:00");

		multiredditDTO = new MultiredditDTO();
		multiredditDTO.setMultiredditId("multiredditId");
		multiredditDTO.setMultireddit("multireddit");
		multiredditDTO.setSubreddit("subreddit");
		multiredditDTOs = new ArrayList<>();
		multiredditDTOs.add(multiredditDTO);
		
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
		when(multiredditHelpers.sendGetMultiredditRequest(ArgumentMatchers.any(UserDTO.class))).thenReturn(response);

		UserDTO userDTO = new UserDTO();
		List<MultiredditDTO> multireddits = multiredditService.fetchMultireddits(userDTO);
		
		assertEquals(multiredditDTOs.get(0).getMultiredditId(), multireddits.get(0).getMultiredditId());
		assertEquals(multiredditDTOs.get(0).getMultireddit(), multireddits.get(0).getMultireddit());
		assertEquals(multiredditDTOs.get(0).getSubreddit(), multireddits.get(0).getSubreddit());
	}
	
}
