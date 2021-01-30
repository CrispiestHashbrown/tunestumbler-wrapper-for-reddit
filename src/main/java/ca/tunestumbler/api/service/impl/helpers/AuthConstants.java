package ca.tunestumbler.api.service.impl.helpers;

public final class AuthConstants {
	
	private AuthConstants() {};
	
	public static final String BASE_URL = "https://www.reddit.com";
	public static final String AUTH_URI = "/api/v1/authorize";
	public static final String CLIENT_ID_PARAMETER = "client_id=VvztT4RO6UUmAA";
	public static final String RESPONSE_TYPE_PARAMETER = "response_type=code";
	public static final String STATE_PARAMETER = "state=";
	public static final String REDIRECT_URI_PARAMETER = "redirect_uri=";
//	public static final String REDIRECT_URI = "https://www.tunestumbler.com/";
	public static final String REDIRECT_URI = "http://localhost:8080/";
	public static final String DURATION_PARAMETER = "duration=permanent";
	public static final String SCOPE_PARAMETER = "scope=read,history,vote,save,account,subscribe,mysubreddits";
	
	public static final String ACCESS_TOKEN_URI = "/api/v1/access_token";
	public static final String USER_AGENT_HEADER = "web:ca.tunestumbler.api:v1.0.0 (by /u/CrispiestHashbrown)";
	public static final String REDDIT_LIFETIME_HEADER = "Reddit-Lifetime";
	
	public static final String AUTH_HEADER = "Basic ";
	
	public static final String GRANT_TYPE_HEADER = "grant_type";
	public static final String AUTH_CODE_GRANT = "authorization_code";
	public static final String REFRESH_TOKEN_GRANT = "refresh_token";
	public static final String CODE_HEADER = "code";
	public static final String REDIRECT_URI_HEADER = "redirect_uri";
	public static final String REFRESH_TOKEN_HEADER = "refresh_token";
	
	public static final String VALID_SCOPES = "account history mysubreddits read save subscribe vote";
	
}
