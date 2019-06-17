package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "filtered")
public class FilteredResultsEntity implements Serializable {
	private static final long serialVersionUID = 5802742836992790074L;

	@GeneratedValue
	@Column(nullable = false, unique = true)
	private long id;

	@Id
	@Column(nullable = false, unique = true)
	private String filteredResultsId;

	@ManyToOne
	@JoinColumn(name = "users_userId")
	private UserEntity userEntity;

	@ManyToOne
	@JoinColumn(name = "filters_filtersId")
	private FiltersEntity filtersEntity;

	@ManyToOne
	@JoinColumn(name = "results_resultsId")
	private ResultsEntity resultsEntity;

	@Column(nullable = false)
	private String lastModified;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public FiltersEntity getFiltersEntity() {
		return filtersEntity;
	}

	public void setFiltersEntity(FiltersEntity filtersEntity) {
		this.filtersEntity = filtersEntity;
	}

	public String getFilteredResultsId() {
		return filteredResultsId;
	}

	public void setFilteredResultsId(String filteredResultsId) {
		this.filteredResultsId = filteredResultsId;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public ResultsEntity getResultsEntity() {
		return resultsEntity;
	}

	public void setResultsEntity(ResultsEntity resultsEntity) {
		this.resultsEntity = resultsEntity;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}
