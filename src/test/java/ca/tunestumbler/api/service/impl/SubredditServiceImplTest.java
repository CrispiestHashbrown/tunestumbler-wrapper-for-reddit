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

import ca.tunestumbler.api.service.impl.helpers.SubredditHelpers;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

public class SubredditServiceImplTest {

	@InjectMocks
	SubredditServiceImpl subredditServiceImpl;

	@Mock
	SubredditHelpers subredditHelpers;

	UserDTO userDTO;
	String afterId = "afterId";
	String beforeId = "beforeId";
	List<SubredditDTO> subredditDTOs;
	SubredditDTO subredditDTO;
	String subreddit = "subreddit";
	SubredditFetchResponseModel response;

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

		subredditDTOs = new ArrayList<>();
		subredditDTO = new SubredditDTO();
		subredditDTO.setSubredditId("subredditId");
		subredditDTO.setAfterId(afterId);
		subredditDTO.setBeforeId(beforeId);
		subredditDTO.setSubreddit(subreddit);
		subredditDTOs.add(subredditDTO);

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
		when(subredditHelpers.sendGetSubredditRequest(ArgumentMatchers.any(UserDTO.class))).thenReturn(response);
		when(subredditHelpers.createNewSubredditDTO(anyString(),
				ArgumentMatchers.any(SubredditFetchResponseModel.class))).thenReturn(subredditDTO);

		List<SubredditDTO> subreddits = subredditServiceImpl.fetchSubreddits(userDTO);

		assertEquals(subredditDTOs.get(0).getSubredditId(), subreddits.get(0).getSubredditId());
		assertEquals(subredditDTOs.get(0).getAfterId(), subreddits.get(0).getAfterId());
		assertEquals(subredditDTOs.get(0).getBeforeId(), subreddits.get(0).getBeforeId());
		assertEquals(subredditDTOs.get(0).getSubreddit(), subreddits.get(0).getSubreddit());
	}
}
