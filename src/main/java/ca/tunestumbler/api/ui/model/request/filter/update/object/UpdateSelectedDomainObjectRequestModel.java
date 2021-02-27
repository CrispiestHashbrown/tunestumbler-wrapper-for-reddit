package ca.tunestumbler.api.ui.model.request.filter.update.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateSelectedDomainObjectRequestModel {
	private String selectedDomainId;

	@NotBlank
	@NotNull
	@NotEmpty
	private String selectedDomain;

	public String getSelectedDomainId() {
		return selectedDomainId;
	}

	public void setSelectedDomainId(String selectedDomainId) {
		this.selectedDomainId = selectedDomainId;
	}

	public String getSelectedDomain() {
		return selectedDomain;
	}

	public void setSelectedDomain(String selectedDomain) {
		this.selectedDomain = selectedDomain;
	}

}
