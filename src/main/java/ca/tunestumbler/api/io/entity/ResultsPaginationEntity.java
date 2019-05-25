package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.tunestumbler.api.shared.dto.ResultsDTO;

public class ResultsPaginationEntity implements Serializable {
	private static final long serialVersionUID = -1635177118264295579L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String resultsPaginationId;

	@Column
	private String beforeResultsId;

	@Column
	private String afterResultsId;

	@ManyToOne
	@JoinColumn(name = "results_id")
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
