package ca.tunestumbler.api.ui.model.response;

import java.util.List;

public class ErrorsResponse {
	private List<ErrorObject> errors;

	public List<ErrorObject> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorObject> errors) {
		this.errors = errors;
	}
}
