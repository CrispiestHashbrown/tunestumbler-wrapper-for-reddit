package ca.tunestumbler.api.ui.model.request.filter.create;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.tunestumbler.api.ui.model.request.filter.create.object.CreateFiltersObjectRequestModel;

public class CreateFiltersRequestModel {
	@NotNull
	@NotEmpty
	@Valid
	private List<CreateFiltersObjectRequestModel> filters;

	public List<CreateFiltersObjectRequestModel> getFilters() {
		return filters;
	}

	public void setFilters(List<CreateFiltersObjectRequestModel> filters) {
		this.filters = filters;
	}

}
