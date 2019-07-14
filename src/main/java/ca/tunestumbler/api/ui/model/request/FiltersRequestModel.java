package ca.tunestumbler.api.ui.model.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.tunestumbler.api.ui.model.request.filters.FiltersObjectRequestModel;

public class FiltersRequestModel {

	@NotNull(message = "Filters cannot be null")
	@NotEmpty(message = "Filters cannot be empty")
	private List<FiltersObjectRequestModel> filters;

	public List<FiltersObjectRequestModel> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersObjectRequestModel> filters) {
		this.filters = filters;
	}

}
