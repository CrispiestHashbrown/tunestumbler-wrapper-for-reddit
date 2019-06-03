package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

import ca.tunestumbler.api.io.entity.UserEntity;

public class RedditAuthDTO implements Serializable {
	private static final long serialVersionUID = 3392082910490313018L;
	private String authId;
	private String accessToken;
	private String refreshToken;
	private String scope;
	private UserEntity userEntity;

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

}
