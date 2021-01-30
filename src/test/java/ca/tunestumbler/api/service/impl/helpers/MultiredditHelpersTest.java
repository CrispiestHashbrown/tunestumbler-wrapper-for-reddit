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
import ca.tunestumbler.api.shared.dto.MultiredditDTO;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditDataModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditDataSubredditModel;
import ca.tunestumbler.api.ui.model.response.multireddit.MultiredditFetchResponseModel;

public class MultiredditHelpersTest {
	
	@InjectMocks
	MultiredditHelpers multiredditHelpers;
	
	@Mock
	SharedUtils sharedUtils;
	
	MultiredditDTO multiredditDTO;
	String multiredditId = "multiredditId";
	String multireddit = "multireddit";
	String subreddit = "subreddit";
	MultiredditFetchResponseModel multiredditResponse;
	MultiredditDataSubredditModel subredditModel;	
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		multiredditDTO.setMultiredditId(multiredditId);
		multiredditDTO.setMultireddit(multireddit);
		multiredditDTO.setSubreddit(subreddit);
		
		subredditModel.setName(subreddit);
		
		List<MultiredditDataSubredditModel> subreddits = new ArrayList<>();
		subreddits.add(subredditModel);
		
		MultiredditDataModel data = new MultiredditDataModel();
		data.setName(multireddit);
		data.setSubreddits(subreddits);
		
		multiredditResponse.setData(data);
	}
	
	@Test
	void testCreateNewMultiredditDTO() {
		when(sharedUtils.generateMultiredditId(anyInt())).thenReturn(multiredditId);
		
		MultiredditDTO newMultiredditDTO = multiredditHelpers.createNewMultiredditDTO(multiredditResponse, subredditModel);
		
		assertEquals(multiredditDTO.getMultiredditId(), newMultiredditDTO.getMultiredditId());
		assertEquals(multiredditDTO.getMultireddit(), newMultiredditDTO.getMultireddit());
		assertEquals(multiredditDTO.getSubreddit(), newMultiredditDTO.getSubreddit());
	}
	
}
