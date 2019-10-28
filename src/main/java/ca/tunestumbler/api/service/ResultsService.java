package ca.tunestumbler.api.service;

import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ResultsResponseModel;

public interface ResultsService {
	ResultsResponseModel fetchResults(UserDTO user, String orderBy);

	ResultsResponseModel fetchNextResults(UserDTO user, String nextUri, String nextAfterId);
}
