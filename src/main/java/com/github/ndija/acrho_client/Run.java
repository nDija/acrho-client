package com.github.ndija.acrho_client;

import java.math.BigDecimal;
import java.util.Date;

public class Run {

	private Long id;
	
	private Long orgId;
	
	private String name;
	
	private String type;

	private Date date;
	
	private BigDecimal distance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString() {
		return "id: " + id
				+ ", orgId: " + orgId
				+ ", name: " + name
				+ ", distance: " + distance
				+ ", type: " + type
				+ ", date: " + date.toString();
	}
}
