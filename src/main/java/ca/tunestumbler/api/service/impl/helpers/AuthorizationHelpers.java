package ca.tunestumbler.api.service.impl.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.security.AuthenticationFacade;
import ca.tunestumbler.api.exceptions.BadRequestException;

@Component
public class AuthorizationHelpers {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationFacade authenticationFacade;

	public void isAuthorized(String userId) {
		String username = userRepository.findEmailByUserId(userId);
		String authentication = authenticationFacade.getAuthentication().getName();
		if (!authentication.equals(username)) {
			throw new BadRequestException();
		}
	}

}
