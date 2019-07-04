package ca.tunestumbler.api.service;

import java.util.List;

import ca.tunestumbler.api.shared.dto.AggregateDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface AggregateService {
	List<AggregateDTO> createAggregateByUserId(UserDTO user);

	List<AggregateDTO> getAggregateByUserId(String userId);

	List<AggregateDTO> updateAggregateByUserId(UserDTO user);
}
