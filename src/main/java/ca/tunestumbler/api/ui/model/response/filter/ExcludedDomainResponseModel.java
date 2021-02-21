package ca.tunestumbler.api.ui.model.response.filter;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.filter.object.ExcludedDomainObjectResponseModel;

public class ExcludedDomainResponseModel {
	List<ExcludedDomainObjectResponseModel> excludedDomains;

	public List<ExcludedDomainObjectResponseModel> getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(List<ExcludedDomainObjectResponseModel> excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

}
