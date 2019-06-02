package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class AuthStateDTO implements Serializable {
	private static final long serialVersionUID = 3907437247642169765L;
	private String stateId;
	private String stateCode;
	private Boolean isValidated;
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
