package ca.tunestumbler.api.ui.model.response.filter;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.filter.object.SelectedDomainObjectResponseModel;

public class SelectedDomainResponseModel {
	List<SelectedDomainObjectResponseModel> selectedDomains;

	public List<SelectedDomainObjectResponseModel> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<SelectedDomainObjectResponseModel> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

}
