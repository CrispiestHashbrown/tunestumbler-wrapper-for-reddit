package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

public class AuthStateEntity implements Serializable {
	private static final long serialVersionUID = 2081466087724011120L;

	@Id
	@Column(nullable = false, unique = true)
	private String stateId;

	@Column(nullable = false, unique = true)
	private String stateCode;

	@Column(nullable = false)
	private Boolean isValidated = false;

	@Column(nullable = false)
	private String lastModified;

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Boolean getIsValidated() {
		return isValidated;
	}

	public void setIsValidated(Boolean isValidated) {
		this.isValidated = isValidated;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}
