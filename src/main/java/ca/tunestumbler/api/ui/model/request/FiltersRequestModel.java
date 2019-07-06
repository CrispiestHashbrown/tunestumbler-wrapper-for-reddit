package ca.tunestumbler.api.ui.model.request;

import java.util.List;

import ca.tunestumbler.api.ui.model.request.filters.FiltersObjectRequestModel;

public class FiltersRequestModel {
	private List<FiltersObjectRequestModel> filters;

	public List<FiltersObjectRequestModel> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersObjectRequestModel> filters) {
		this.filters = filters;
	}

}
