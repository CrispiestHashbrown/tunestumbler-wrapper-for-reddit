package ca.tunestumbler.api.ui.model.request.filter.create.object;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateSelectedDomainObjectRequestModel {
	@NotBlank
	@NotNull
	@NotEmpty
	private String selectedDomain;

	public String getSelectedDomain() {
		return selectedDomain;
	}

	public void setSelectedDomain(String selectedDomain) {
		this.selectedDomain = selectedDomain;
	}

}
