package com.github.ndija.acrho_client.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.ndija.acrho_client.Run;
import com.github.ndija.acrho_client.exception.AcrhoConnectionException;

public class AcrhoService {

	private static final Logger log = Logger.getLogger(AcrhoService.class);
	
	private static final String PATTERN_SELECT_RUN = "&nbsp;((?:\\w|\\s)*)\\s-\\s(\\d{2}\\/\\d{2}\\/\\d{4})\\s\\((\\d{2}[\\.]*[\\d]*)(?:\\w|\\s)*\\)";
	
	private static final String PATTERN_DATE = "dd/MM/yyyy";
	
	public static List<Run> getRuns() throws AcrhoConnectionException, ParseException {
		Pattern p = Pattern.compile(PATTERN_SELECT_RUN);
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
		String url = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUNS);
		InputStream is = HttpService.get(url);
		String response = null;
		try {
			response = IOUtils.toString(is, StandardCharsets.UTF_8.name());
			is.close();
		} catch (IOException e) {
			throw new AcrhoConnectionException("Can't close inputStream on get: " + url, e);
		}
		log.debug(response);
		Document doc = Jsoup.parse(response);
		Elements select = doc.getElementsByAttributeValue("name", "ant_search_courses");
		Elements options = select.get(0).getElementsByTag("option");
		log.debug("Found courses: " + (options.size()-1));
		List<Run> runs = new ArrayList<>();
		for (Element option : options) {
			Long idCourse = Long.valueOf(option.attr("value"));
			if(idCourse == 0) continue;
			Run run = new Run();
			run.setId(idCourse);
			String label = option.childNode(0).outerHtml().trim();
			Matcher m = p.matcher(label);
			if(m.matches()) {
				run.setName(WordUtils.capitalizeFully(m.group(1).trim()));
				run.setDate(sdf.parse(m.group(2)));
				run.setDistance(new BigDecimal(m.group(3)));
				runs.add(run);
				log.debug("Run: " + run.toString());
			}
		}
		return runs;
	}
	
	public static void getResult(Long runId) throws AcrhoConnectionException {
		String url = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUN);
		String parameters = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUN_PARAMETERS);
		parameters = parameters.replaceAll(":runId", String.valueOf(runId));
		InputStream is = HttpService.post(url, parameters);
		String response = null;
		try {
			response = IOUtils.toString(is, StandardCharsets.ISO_8859_1.name());
			is.close();
		} catch (IOException e) {
			throw new AcrhoConnectionException("Can't close inputStream on get: " + url, e);
		}
		log.debug(response);
		Document doc = Jsoup.parse(response);
		Elements table = doc.getElementsByAttributeValue("class", "speResults");
		Elements tbody = table.get(0).getElementsByTag("tbody");
		Elements trs = tbody.get(0).getElementsByTag("tr");
		log.debug("Results count: " + (trs.size() - 1));
		for (int i = 1; i < trs.size(); i++) {
			Elements tds = trs.get(i).getElementsByTag("td");
			for (Element td : tds) {
				log.debug(td.text());
			}
		}
	}
}
