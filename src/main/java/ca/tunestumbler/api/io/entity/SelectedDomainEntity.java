package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "selected_domains")
public class SelectedDomainEntity implements Serializable {
	private static final long serialVersionUID = 334934446447899609L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(nullable = false, unique = true)
	private String selectedDomainId;

	@Column(name = "parent_filters_id")
	private String filtersId;

	@Column()
	private String userId;

	@Column(length = 50)
	private String selectedDomain;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSelectedDomainId() {
		return selectedDomainId;
	}

	public void setSelectedDomainId(String selectedDomainId) {
		this.selectedDomainId = selectedDomainId;
	}

	public String getFiltersId() {
		return filtersId;
	}

	public void setFiltersId(String filtersId) {
		this.filtersId = filtersId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSelectedDomain() {
		return selectedDomain;
	}

	public void setSelectedDomain(String selectedDomain) {
		this.selectedDomain = selectedDomain;
	}

}
