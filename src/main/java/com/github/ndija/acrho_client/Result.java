package com.github.ndija.acrho_client;

import java.math.BigDecimal;

public class Result {
	
	private Integer position;
	private String name;
	private String urlProfil;
	private String team;
	private Long time;
	private Long avg;
	private BigDecimal speed;
	private Integer points;
	private String category;
	
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlProfil() {
		return urlProfil;
	}
	public void setUrlProfil(String urlProfil) {
		this.urlProfil = urlProfil;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getAvg() {
		return avg;
	}
	public void setAvg(Long avg) {
		this.avg = avg;
	}
	public BigDecimal getSpeed() {
		return speed;
	}
	public void setSpeed(BigDecimal speed) {
		this.speed = speed;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "Result [position=" + position + ", name=" + name + ", urlProfil=" + urlProfil + ", team=" + team
				+ ", time=" + time + ", avg=" + avg + ", speed=" + speed + ", points=" + points + ", category="
				+ category + ", hashCode()=" + hashCode() + "]";
	}

}
