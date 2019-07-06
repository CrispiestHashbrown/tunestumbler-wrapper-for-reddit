package ca.tunestumbler.api.ui.model.response;

import java.util.List;

import ca.tunestumbler.api.ui.model.response.filters.FiltersObjectResponseModel;

public class FiltersResponseModel {
	private List<FiltersObjectResponseModel> filters;

	public List<FiltersObjectResponseModel> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersObjectResponseModel> filters) {
		this.filters = filters;
	}

}
