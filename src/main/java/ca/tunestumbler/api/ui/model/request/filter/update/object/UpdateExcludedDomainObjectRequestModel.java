package ca.tunestumbler.api.ui.model.request.filter.update.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateExcludedDomainObjectRequestModel {
	private String excludedDomainId;

	@NotBlank
	@NotNull
	@NotEmpty
	private String excludedDomain;

	public String getExcludedDomainId() {
		return excludedDomainId;
	}

	public void setExcludedDomainId(String excludedDomainId) {
		this.excludedDomainId = excludedDomainId;
	}

	public String getExcludedDomain() {
		return excludedDomain;
	}

	public void setExcludedDomain(String excludedDomain) {
		this.excludedDomain = excludedDomain;
	}

}
