package ca.tunestumbler.api.service;

import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;

public interface AuthValidationService {
	AuthValidationDTO createAuthState(UserDTO user);

	AuthValidationDTO getAuthState(String stateId);

	AuthValidationDTO updateState(String stateId, String code);

	void deleteState(String stateId);
}
