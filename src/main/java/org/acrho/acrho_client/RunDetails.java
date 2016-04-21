package org.acrho.acrho_client;

import static javax.swing.text.html.HTML.Attribute.VALUE;
import static org.acrho.acrho_client.IConstants.PATTERN_DATE;
import static org.acrho.acrho_client.IConstants.PATTERN_SELECT_RUN;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acrho.acrho_client.exception.AcrhoException;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

public class RunDetails {

	private static final Logger log = Logger.getLogger(RunDetails.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
	
	private static Pattern p = Pattern.compile(PATTERN_SELECT_RUN);
	
	private Long id;
	
	private String name;
	
	private String type;

	private Date date;
	
	private BigDecimal distance;

	public RunDetails(Element option) throws AcrhoException {
		Long idCourse = Long.valueOf(option.attr(VALUE.toString()));
		if (idCourse == 0)
			return;
		id = idCourse;
		String label = option.childNode(0).outerHtml().trim();
		Matcher m = p.matcher(label);
		if (m.matches()) {
			name = WordUtils.capitalizeFully(m.group(1).trim());
			try {
				date = sdf.parse(m.group(2));
			} catch (ParseException e) {
				throw new AcrhoException("Error when parsing date: " + m.group(2), e);
			}
			distance = new BigDecimal(m.group(3));
			log.debug("Run: " + this.toString());
		}
	}
	
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RunDetails [sdf=").append(sdf).append(", id=").append(id).append(", name=").append(name)
				.append(", type=").append(type).append(", date=").append(date).append(", distance=").append(distance)
				.append("]");
		return builder.toString();
	}
}
