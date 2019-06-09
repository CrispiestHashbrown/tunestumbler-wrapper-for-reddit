package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class AuthValidationDTO implements Serializable {
	private static final long serialVersionUID = 3907437247642169765L;
	private String stateId;
	private Boolean validated = false;
	private String code;
	private String lastModified;
	private UserDTO userDTO;

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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
