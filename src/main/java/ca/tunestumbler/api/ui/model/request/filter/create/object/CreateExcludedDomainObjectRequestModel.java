package ca.tunestumbler.api.ui.model.request.filter.create.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateExcludedDomainObjectRequestModel {
	@NotBlank
	@NotNull
	@NotEmpty
	private String excludedDomain;

	public String getExcludedDomain() {
		return excludedDomain;
	}

	public void setExcludedDomain(String excludedDomain) {
		this.excludedDomain = excludedDomain;
	}

}
