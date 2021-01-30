package ca.tunestumbler.api.service;

import org.springframework.http.HttpHeaders;

import ca.tunestumbler.api.shared.dto.AuthValidationDTO;

public interface AuthValidationService {
	AuthValidationDTO createAuthState(String userId);

	HttpHeaders createHandlerHeaders(String state, String code);

	HttpHeaders createRefreshTokenHeaders(String userId);
}
