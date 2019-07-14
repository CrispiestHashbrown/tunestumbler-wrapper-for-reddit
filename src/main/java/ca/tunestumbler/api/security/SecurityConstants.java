package ca.tunestumbler.api.security;

import ca.tunestumbler.api.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 31556952000L; // 1 year
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String HANDLER_URL = "/auth/handler";

	public static String getTokenSecret() {
		ApplicationProperties applicationProperties = (ApplicationProperties) SpringApplicationContext
				.getBean("ApplicationProperties");
		return applicationProperties.getTokenSecret();
	}

	public static String getAuth() {
		ApplicationProperties applicationProperties = (ApplicationProperties) SpringApplicationContext
				.getBean("ApplicationProperties");
		return applicationProperties.getAuth();
	}

}
