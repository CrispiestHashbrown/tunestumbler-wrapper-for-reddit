package ca.tunestumbler.api.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {
	Authentication getAuthentication();
}
