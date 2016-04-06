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

import com.github.ndija.acrho_client.ResultDetails;
import com.github.ndija.acrho_client.RunDetails;
import com.github.ndija.acrho_client.RunnerDetails;
import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.exception.AcrhoException;

public class AcrhoService {

	private static final Logger log = Logger.getLogger(AcrhoService.class);

	private static final String PATTERN_SELECT_RUN = "&nbsp;((?:\\w|\\s)*)\\s-\\s(\\d{2}\\/\\d{2}\\/\\d{4})\\s\\((\\d{2}[\\.]*[\\d]*)(?:\\w|\\s)*\\)";

	private static final String PATTERN_SELECT_ID_RUNNER = "cust_participants.php\\?langue\\=french\\&cle_menus=1187970092\\&cle_data=(\\d*)\\&origin\\=cust_resultats\\.php";

	private static final String PATTERN_DATE = "dd/MM/yyyy";

	/**
	 * Call by http get method acrho.org menu result by runs and parse html and
	 * get list in select box
	 * 
	 * @return List of runs of the current year
	 * @throws AcrhoConnectionException
	 *             when failing to connect to arcrho.org
	 * @throws AcrhoException
	 *             when bad parsing date of the run
	 * @author nDija
	 * 
	 */
	public static List<RunDetails> getRuns() throws AcrhoConnectionException, AcrhoException {
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
		log.debug("Found courses: " + (options.size() - 1));
		List<RunDetails> runs = new ArrayList<>();
		for (Element option : options) {
			Long idCourse = Long.valueOf(option.attr("value"));
			if (idCourse == 0)
				continue;
			RunDetails run = new RunDetails();
			run.setId(idCourse);
			String label = option.childNode(0).outerHtml().trim();
			Matcher m = p.matcher(label);
			if (m.matches()) {
				run.setName(WordUtils.capitalizeFully(m.group(1).trim()));
				try {
					run.setDate(sdf.parse(m.group(2)));
				} catch (ParseException e) {
					throw new AcrhoException("Error when parsing date: " + m.group(2), e);
				}
				run.setDistance(new BigDecimal(m.group(3)));
				runs.add(run);
				log.debug("Run: " + run.toString());
			}
		}
		return runs;
	}

	/**
	 * 
	 * @param runId
	 *            The id of the run
	 * @throws AcrhoConnectionException
	 *             when failing to connect to arcrho.org
	 * @author nDija
	 */
	public static List<ResultDetails> getResult(Long runId) throws AcrhoConnectionException {
		Pattern p = Pattern.compile(PATTERN_SELECT_ID_RUNNER);
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

		List<ResultDetails> results = new ArrayList<>();
		for (int i = 1; i < trs.size(); i++) {

			Elements tds = trs.get(i).getElementsByTag("td");

			Integer position = Integer.parseInt(tds.get(0).text());
			String name = tds.get(1).text().replaceAll("&nbsp;", "");
			String urlProfil = tds.get(1).getElementsByTag("a").get(0).attr("href");
			String team = tds.get(2).text();
			Long time = getMillis(tds.get(3).text());
			Long avg = getMillis(tds.get(4).text());
			BigDecimal speed = new BigDecimal(tds.get(5).text().replaceAll(",", "."));
			Integer points = new Integer(tds.get(6).text());
			String category = tds.get(7).text();

			ResultDetails result = new ResultDetails();
			result.setAvg(avg);
			result.setCategory(category);
			result.setName(name);
			result.setPoints(points);
			result.setPosition(position);
			result.setSpeed(speed);
			result.setTeam(team);
			result.setTime(time);
			result.setUrlProfil(urlProfil);

			Matcher m = p.matcher(urlProfil);
			if (m.matches()) {
				result.setIdRunner(Long.valueOf(m.group(1)));
			}

			log.debug(result.toString());

			results.add(result);
		}

		return results;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AcrhoConnectionException
	 * @throws AcrhoException when parsing a date
	 */
	public static RunnerDetails getRunnerDetails(Long id) throws AcrhoConnectionException, AcrhoException {
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
		String url = AcrhoProperties.get(AcrhoProperties.URL_RUNNER_DETAILS);
		url = url.replaceAll(":id", String.valueOf(id));
		InputStream is = HttpService.get(url);
		String response = null;
		try {
			response = IOUtils.toString(is, StandardCharsets.ISO_8859_1.name());
			is.close();
		} catch (IOException e) {
			throw new AcrhoConnectionException("Can't close inputStream on get: " + url, e);
		}
		log.debug(response);

		Document doc = Jsoup.parse(response);
		String name = doc.getElementsByAttributeValue("class", "par1descr").get(0).getElementsByTag("h1").text();
		Elements details = doc.getElementsByAttributeValue("class", "par1descr").get(0).getElementsByAttributeValue("class", "speValue");
		
		RunnerDetails runner = new RunnerDetails();
		runner.setName(name);
		runner.setBib(Integer.parseInt(details.get(3).text()));
		try {
			runner.setBirthDate(sdf.parse(details.get(0).text()));
		} catch (ParseException e) {
			throw new AcrhoException("Error when parsing date: " + details.get(0).text(), e);
		}
		runner.setCategory(details.get(1).text());
		runner.setTeam(details.get(2).text());
		return runner;
	}

	private static Long getMillis(String time) {
		String[] h = time.split("h");
		String[] m = time.split("m");

		Long timeInMillis = 0L;
		if (h.length != 1) {
			timeInMillis = Long.valueOf(h[0]) * 60 * 60 * 1000;
			m = h[1].split("m");
		}

		timeInMillis += Long.valueOf(m[0]) * 60 * 1000;
		timeInMillis += Long.valueOf(m[1].replaceAll("s", "")) * 1000;
		return timeInMillis;
	}
}
