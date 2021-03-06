package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "validation")
public class AuthValidationEntity implements Serializable {
	private static final long serialVersionUID = 2081466087724011120L;

	@Id
	@Column(nullable = false, unique = true)
	private String stateId;

	@Column(nullable = false)
	private Boolean validated = false;

	@Column(unique = true)
	private String code;

	@Column(nullable = false)
	private String lastModified;

	@Column()
	private String userId;

	@Column(length = 350, unique = true)
	private String authorizationUrl;

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthorizationUrl() {
		return authorizationUrl;
	}

	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}

}
