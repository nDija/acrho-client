package org.acrho.acrho_client;

import java.util.Date;

public class RunnerDetails {

	private String name;
	
	private Date birthDate;
	
	private String category;
	
	private String team;
	
	private Integer bib;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getBib() {
		return bib;
	}

	public void setBib(Integer bib) {
		this.bib = bib;
	}

	@Override
	public String toString() {
		return "Runner [name=" + name + ", birthDate=" + birthDate + ", category=" + category + ", team=" + team
				+ ", bib=" + bib + "]";
	}
}
