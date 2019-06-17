package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class FilteredResultsDTO implements Serializable {
	private static final long serialVersionUID = -6865155891339745400L;
	private long id;
	private String filteredResultsId;
	private UserDTO userDTO;
	private FiltersDTO filtersDTO;
	private ResultsDTO resultsDTO;
	private String lastModified;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilteredResultsId() {
		return filteredResultsId;
	}

	public void setFilteredResultsId(String filteredResultsId) {
		this.filteredResultsId = filteredResultsId;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public FiltersDTO getFiltersDTO() {
		return filtersDTO;
	}

	public void setFiltersDTO(FiltersDTO filtersDTO) {
		this.filtersDTO = filtersDTO;
	}

	public ResultsDTO getResultsDTO() {
		return resultsDTO;
	}

	public void setResultsDTO(ResultsDTO resultsDTO) {
		this.resultsDTO = resultsDTO;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}
