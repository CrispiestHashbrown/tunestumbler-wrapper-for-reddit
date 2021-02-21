package ca.tunestumbler.api.ui.model.response.filter;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.filter.object.FiltersObjectResponseModel;

public class FiltersResponseModel {
	private List<FiltersObjectResponseModel> filters;

	public List<FiltersObjectResponseModel> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersObjectResponseModel> filters) {
		this.filters = filters;
	}

}
