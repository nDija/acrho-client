package org.acrho.acrho_client.service;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.swing.text.html.HTML.Attribute.CLASS;
import static javax.swing.text.html.HTML.Attribute.NAME;
import static javax.swing.text.html.HTML.Attribute.VALUE;
import static javax.swing.text.html.HTML.Tag.H1;
import static javax.swing.text.html.HTML.Tag.OPTION;
import static javax.swing.text.html.HTML.Tag.TD;
import static javax.swing.text.html.HTML.Tag.TR;
import static org.acrho.acrho_client.IConstants.ANT_SEARCH_COURSES;
import static org.acrho.acrho_client.IConstants.PAR1DESCR;
import static org.acrho.acrho_client.IConstants.PATTERN_DATE;
import static org.acrho.acrho_client.IConstants.SPERESULTS;
import static org.acrho.acrho_client.IConstants.SPEVALUE;
import static org.acrho.acrho_client.IConstants.TBODY;
import static org.acrho.acrho_client.IConstants._0;
import static org.acrho.acrho_client.service.IAcrhoProperties.PARAM_ID;
import static org.acrho.acrho_client.service.IAcrhoProperties.PARAM_RUN_ID;
import static org.acrho.acrho_client.service.IAcrhoProperties.URL_RESULT_RUN;
import static org.acrho.acrho_client.service.IAcrhoProperties.URL_RESULT_RUNS;
import static org.acrho.acrho_client.service.IAcrhoProperties.URL_RESULT_RUN_PARAMETERS;
import static org.acrho.acrho_client.service.IAcrhoProperties.URL_RUNNER_DETAILS;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.acrho.acrho_client.ResultDetails;
import org.acrho.acrho_client.RunDetails;
import org.acrho.acrho_client.RunnerDetails;
import org.acrho.acrho_client.exception.AcrhoConnectionException;
import org.acrho.acrho_client.exception.AcrhoException;
import org.acrho.acrho_client.exception.UtilException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AcrhoService {

	private static final Logger log = Logger.getLogger(AcrhoService.class);


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
		String url = AcrhoProperties.get(URL_RESULT_RUNS);
		InputStream is = HttpService.get(url);
		String response = null;
		try {
			response = IOUtils.toString(is, UTF_8.name());
			is.close();
		} catch (IOException e) {
			throw new AcrhoConnectionException("Can't close inputStream on get: " + url, e);
		}
		log.debug(response);
		Elements options = Jsoup.parse(response)
				.getElementsByAttributeValue(NAME.toString(), ANT_SEARCH_COURSES).get(0)
				.getElementsByTag(OPTION.toString());
		log.debug("Found courses: " + (options.size() - 1));
		List<RunDetails> runs = options.stream()
				.filter((option) -> !option.attr(VALUE.toString()).equals(_0))
				.map(UtilException.rethrowFunction(option -> new RunDetails(option)))
				.collect(Collectors.toList());
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
		String url = AcrhoProperties.get(URL_RESULT_RUN);
		String parameters = AcrhoProperties.get(URL_RESULT_RUN_PARAMETERS);
		parameters = parameters.replaceAll(PARAM_RUN_ID, String.valueOf(runId));
		InputStream is = HttpService.post(url, parameters);
		String response = null;
		try {
			response = IOUtils.toString(is, ISO_8859_1.name());
			is.close();
		} catch (IOException e) {
			throw new AcrhoConnectionException("Can't close inputStream on get: " + url, e);
		}

		log.debug(response);

		Document doc = Jsoup.parse(response);
		Elements trResults = doc.getElementsByAttributeValue(CLASS.toString(), SPERESULTS)
			.get(0).getElementsByTag(TBODY)
			.get(0).getElementsByTag(TR.toString());
		
		trResults.remove(0);
		log.debug("Results count: " + trResults.size());
		List<ResultDetails> results = trResults.stream()
			.map(tr -> tr.getElementsByTag(TD.toString()))
			.map(tds -> new ResultDetails(tds))
			.collect(Collectors.toList());
		
		return results;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws AcrhoConnectionException
	 * @throws AcrhoException
	 *             when parsing a date
	 */
	public static RunnerDetails getRunner(Long id) throws AcrhoConnectionException, AcrhoException {
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
		String url = AcrhoProperties.get(URL_RUNNER_DETAILS);
		url = url.replaceAll(PARAM_ID, String.valueOf(id));
		InputStream is = HttpService.get(url);
		String response = null;
		try {
			response = IOUtils.toString(is, ISO_8859_1.name());
			is.close();
		} catch (IOException e) {
			throw new AcrhoConnectionException("Can't close inputStream on get: " + url, e);
		}
		log.debug(response);

		Document doc = Jsoup.parse(response);
		String name = doc.getElementsByAttributeValue(CLASS.toString(), PAR1DESCR).get(0)
				.getElementsByTag(H1.toString()).text();
		Elements details = doc.getElementsByAttributeValue(CLASS.toString(), PAR1DESCR).get(0)
				.getElementsByAttributeValue(CLASS.toString(), SPEVALUE);

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
}
