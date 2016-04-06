package com.github.ndija.acrho_client;

import java.math.BigDecimal;
import java.util.Date;

public class RunDetails {

	private Long id;
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String toString() {
		return "id: " + id
				+ ", name: " + name
				+ ", distance: " + distance
				+ ", type: " + type
				+ ", date: " + date.toString();
	}
}
