package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class SelectedDomainDTO implements Serializable {
	private static final long serialVersionUID = 8397015457162047622L;
	private String selectedDomainId;
	private String filtersId;
	private String selectedDomain;

	public String getSelectedDomainId() {
		return selectedDomainId;
	}

	public void setSelectedDomainId(String selectedDomainId) {
		this.selectedDomainId = selectedDomainId;
	}

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getSelectedDomain() {
		return selectedDomain;
	}

	public void setSelectedDomain(String selectedDomain) {
		this.selectedDomain = selectedDomain;
	}

}
