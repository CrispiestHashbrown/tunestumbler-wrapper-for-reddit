package ca.tunestumbler.api.shared.dto;

import java.io.Serializable;

public class ExcludedDomainDTO implements Serializable {
	private static final long serialVersionUID = -2724494611954917837L;
	private String excludedDomainId;
	private String filtersId;
	private String excludedDomain;

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

	public String getExcludedDomain() {
		return excludedDomain;
	}

	public void setExcludedDomain(String excludedDomain) {
		this.excludedDomain = excludedDomain;
	}

}
