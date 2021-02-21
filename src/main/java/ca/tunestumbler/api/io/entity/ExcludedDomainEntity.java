package ca.tunestumbler.api.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "excluded_domains")
public class ExcludedDomainEntity implements Serializable {
	private static final long serialVersionUID = -5574989306319868145L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(nullable = false, unique = true)
	private String excludedDomainId;

	@Column(name = "parent_filters_id")
	private String filtersId;

	@Column()
	private String userId;

	@Column(length = 50)
	private String excludedDomain;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExcludedDomainId() {
		return excludedDomainId;
	}

	public void setExcludedDomainId(String excludedDomainId) {
		this.excludedDomainId = excludedDomainId;
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

	public String getExcludedDomain() {
		return excludedDomain;
	}

	public void setExcludedDomain(String excludedDomain) {
		this.excludedDomain = excludedDomain;
	}

}
