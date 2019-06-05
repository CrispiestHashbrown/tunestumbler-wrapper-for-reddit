package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "validation")
public class AuthValidationEntity implements Serializable {
	private static final long serialVersionUID = 2081466087724011120L;

	@Id
	@Column(nullable = false, unique = true)
	private String stateId;

	@Column(nullable = false)
	private Boolean isValidated = false;

	@Column(unique = true)
	private String code;

	@Column(nullable = false)
	private String lastModified;

	@ManyToOne
	@JoinColumn(name = "users_userId")
	private UserEntity userEntity;

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public Boolean getIsValidated() {
		return isValidated;
	}

	public void setIsValidated(Boolean isValidated) {
		this.isValidated = isValidated;
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

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

}