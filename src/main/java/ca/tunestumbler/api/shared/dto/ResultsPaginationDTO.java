package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class ResultsPaginationDTO implements Serializable {
	private static final long serialVersionUID = 1065492302856447841L;
	private long id;
	private String resultsPaginationId;
	private String beforeResultsId;
	private String afterResultsId;
	private ResultsDTO resultsDTO;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResultsPaginationId() {
		return resultsPaginationId;
	}

	public void setResultsPaginationId(String resultsPaginationId) {
		this.resultsPaginationId = resultsPaginationId;
	}

	public String getBeforeResultsId() {
		return beforeResultsId;
	}

	public void setBeforeResultsId(String beforeResultsId) {
		this.beforeResultsId = beforeResultsId;
	}

	public String getAfterResultsId() {
		return afterResultsId;
	}

	public void setAfterResultsId(String afterResultsId) {
		this.afterResultsId = afterResultsId;
	}

	public ResultsDTO getResultsDTO() {
		return resultsDTO;
	}

	public void setResultsDTO(ResultsDTO resultsDTO) {
		this.resultsDTO = resultsDTO;
	}

}
