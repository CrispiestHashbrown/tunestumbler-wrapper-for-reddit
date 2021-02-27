package ca.tunestumbler.api.ui.model.request.filter.update;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ca.tunestumbler.api.ui.model.request.filter.update.object.UpdateFiltersObjectRequestModel;

public class UpdateFiltersRequestModel {
	@NotNull
	@NotEmpty
	@Valid
	private List<UpdateFiltersObjectRequestModel> filters;

	public List<UpdateFiltersObjectRequestModel> getFilters() {
		return filters;
	}

	public void setFilters(List<UpdateFiltersObjectRequestModel> filters) {
		this.filters = filters;
	}

}
