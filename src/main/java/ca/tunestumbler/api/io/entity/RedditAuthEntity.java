package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "auth")
public class RedditAuthEntity implements Serializable {
	private static final long serialVersionUID = 4973442855950016187L;

	@Id
	@Column(nullable = false, unique = true)
	private String authId;

	@Column(nullable = false, unique = true)
	private String accessToken;

	@Column(nullable = false, unique = true)
	private String refreshToken;

	@Column(nullable = false)
	private String scope;

	@OneToOne()
	@JoinColumn(name = "users_userId")
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
