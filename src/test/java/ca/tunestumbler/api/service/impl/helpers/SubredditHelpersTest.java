package ca.tunestumbler.api.service.impl.helpers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.SubredditDTO;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenDataModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataChildrenModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditDataModel;
import ca.tunestumbler.api.ui.model.response.subreddit.SubredditFetchResponseModel;

public class SubredditHelpersTest {
	
	@InjectMocks
	SubredditHelpers subredditHelpers;
	
	@Mock
	SharedUtils sharedUtils;

	SubredditDTO subredditDTO;
	String subredditId = "subredditId";
	String subreddit = "subreddit";
	String afterId = "afterId";
	String beforeId = "beforeId";
	SubredditFetchResponseModel response;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		subredditDTO.setSubredditId(subredditId);
		subredditDTO.setSubreddit(subreddit);
		subredditDTO.setAfterId(afterId);
		subredditDTO.setBeforeId(beforeId);
		
		SubredditDataChildrenDataModel data = new SubredditDataChildrenDataModel();
		data.setDisplay_name(subreddit);
		
		SubredditDataChildrenModel child = new SubredditDataChildrenModel();
		child.setData(data);
		
		List<SubredditDataChildrenModel> children = new ArrayList<>();
		children.add(child);
		
		SubredditDataModel subredditData = new SubredditDataModel();
		subredditData.setChildren(children);
		subredditData.setAfter(afterId);
		subredditData.setBefore(beforeId);
		
		response.setData(subredditData);		
	}
	
	@Test
	void testCreateNewSubredditDTO() {
		when(sharedUtils.generateSubredditId(anyInt())).thenReturn(subredditId);
		
		SubredditDTO newSubredditDTO = subredditHelpers.createNewSubredditDTO(subreddit, response);
		
		assertEquals(subredditDTO.getSubredditId(), newSubredditDTO.getSubredditId());
		assertEquals(subredditDTO.getSubreddit(), newSubredditDTO.getSubreddit());
		assertEquals(subredditDTO.getAfterId(), newSubredditDTO.getAfterId());
		assertEquals(subredditDTO.getBeforeId(), newSubredditDTO.getBeforeId());
	}
	
}
