package org.acrho.acrho_client;

import static javax.swing.text.html.HTML.Attribute.HREF;
import static javax.swing.text.html.HTML.Tag.A;
import static org.acrho.acrho_client.IConstants.COMA;
import static org.acrho.acrho_client.IConstants.EMPTY_STRING;
import static org.acrho.acrho_client.IConstants.NBSP;
import static org.acrho.acrho_client.IConstants.PATTERN_SELECT_ID_RUNNER;
import static org.acrho.acrho_client.IConstants.POINT;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acrho.acrho_client.service.DateUtils;
import org.apache.log4j.Logger;
import org.jsoup.select.Elements;

public class ResultDetails {
	
	private Integer position;
	private String name;
	private String urlProfil;
	private String team;
	private Long time;
	private Long avg;
	private BigDecimal speed;
	private Integer points;
	private String category;
	private Long idRunner;
	
	private static final Logger log = Logger.getLogger(ResultDetails.class);
	
	private static Pattern p = Pattern.compile(PATTERN_SELECT_ID_RUNNER);
	
	public ResultDetails(Elements tds) {
		Integer position = Integer.parseInt(tds.get(0).text());
		String name = tds.get(1).text().replaceAll(NBSP, EMPTY_STRING);
		String urlProfil = tds.get(1).getElementsByTag(A.toString()).get(0).attr(HREF.toString());
		String team = tds.get(2).text();
		Long time = DateUtils.getMillis(tds.get(3).text());
		Long avg = DateUtils.getMillis(tds.get(4).text());
		BigDecimal speed = new BigDecimal(tds.get(5).text().replaceAll(COMA, POINT));
		Integer points = new Integer(tds.get(6).text());
		String category = tds.get(7).text();

		setAvg(avg);
		setCategory(category);
		setName(name);
		setPoints(points);
		setPosition(position);
		setSpeed(speed);
		setTeam(team);
		setTime(time);
		setUrlProfil(urlProfil);

		Matcher m = p.matcher(urlProfil);
		if (m.matches()) {
			setIdRunner(Long.valueOf(m.group(1)));
		}

		log.debug(toString());
	}
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
	public Long getIdRunner() {
		return idRunner;
	}
	public void setIdRunner(Long idRunner) {
		this.idRunner = idRunner;
	}
	
	@Override
	public String toString() {
		return "Result [position=" + position + ", name=" + name + ", urlProfil=" + urlProfil + ", team=" + team
				+ ", time=" + time + ", avg=" + avg + ", speed=" + speed + ", points=" + points + ", category="
				+ category + ", idRunner=" + idRunner + ", hashCode()=" + hashCode() + "]";
	}
}
