package ca.tunestumbler.api.ui.model.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import ca.tunestumbler.api.shared.dto.FiltersDTO;

public class FiltersRequestModel {

	@NotNull(message = "Filters cannot be null")
	private List<FiltersDTO> filters;

	public List<FiltersDTO> getFilters() {
		return filters;
	}

	public void setFilters(List<FiltersDTO> filters) {
		this.filters = filters;
	}

}
