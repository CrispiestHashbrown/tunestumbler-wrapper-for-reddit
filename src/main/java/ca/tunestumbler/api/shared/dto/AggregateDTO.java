package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;
import java.util.List;

public class AggregateDTO implements Serializable {
	private static final long serialVersionUID = -41686199429484255L;
	private long id;
	private String aggregateId;
	private UserDTO userDTO;
	private SubredditDTO subredditDTO;
	private MultiredditDTO multiredditDTO;
	private String lastModified;
	private List<ResultsDTO> results;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAggregateId() {
		return aggregateId;
	}

	public void setAggregateId(String aggregateId) {
		this.aggregateId = aggregateId;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public SubredditDTO getSubredditDTO() {
		return subredditDTO;
	}

	public void setSubredditDTO(SubredditDTO subredditDTO) {
		this.subredditDTO = subredditDTO;
	}

	public MultiredditDTO getMultiredditDTO() {
		return multiredditDTO;
	}

	public void setMultiredditDTO(MultiredditDTO multiredditDTO) {
		this.multiredditDTO = multiredditDTO;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public List<ResultsDTO> getResults() {
		return results;
	}

	public void setResults(List<ResultsDTO> results) {
		this.results = results;
	}

}
