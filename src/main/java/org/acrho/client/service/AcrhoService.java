package org.acrho.client.service;

import lombok.extern.log4j.Log4j2;
import org.acrho.client.util.AcrhoUtil;
import org.acrho.client.model.AcrhoResult;
import org.acrho.client.model.AcrhoRun;
import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.model.property.AcrhoProperties;
import org.acrho.client.model.property.QueryProperties;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.swing.text.html.HTML.Attribute.CLASS;
import static javax.swing.text.html.HTML.Attribute.NAME;
import static javax.swing.text.html.HTML.Attribute.VALUE;
import static javax.swing.text.html.HTML.Tag.*;
import static org.acrho.client.AcrhoConstant.*;

/**
 * Main service class for retrieving information from the site acrho.org
 *
 * @author Vincent Hullaert
 *
 */
@Log4j2
public class AcrhoService {

	private AcrhoProperties ap = PropertyService.getInstance().getAcrhoProperties();
	private static HttpService httpService = new HttpService();

	/**
	 * Get a list of runs by year
	 *
	 * @param year The year of the runs
	 * @return List of runs of the current year
	 * @author Vincent Hullaert
	 */
	public List<AcrhoRun> getRuns(String year) {
		Map<String, String> parameters = AcrhoUtil.getParameters(ap.getRuns());
		parameters.put(ANT_FILTER_VALUE, year);
		String response = post(ap.getRuns(), parameters);
		Elements options = Jsoup.parse(response)
				.getElementsByAttributeValue(NAME.toString(), ANT_SEARCH_COURSES).get(0)
				.getElementsByTag(OPTION.toString());
		log.debug("Found courses: " + (options.size() - 1));
		return options.stream()
				.filter(option -> !option.attr(VALUE.toString()).equals(STRING_0))
				.map(AcrhoUtil::getRun)
				.collect(Collectors.toList());
	}

	/**
	 * Get a list of results by run id
	 *
	 * @param id
	 *            The id of the run
	 * @return List of results for a run
	 * @author Vincent Hullaert
	 */
	public List<AcrhoResult> getResult(String year, String id) throws IOException {
		HttpURLConnection connection = HttpService.createConnection(ap.getRuns().getUri());
		Map<String, String> parameters = AcrhoUtil.getParameters(ap.getRuns());
		parameters.put(ANT_FILTER_VALUE, year);
		post(ap.getRuns(), parameters);

		parameters = AcrhoUtil.getParameters(ap.getResults());
		parameters.put(ANT_FILTER_VALUE, id);
		String response = post(ap.getResults(), parameters);
		Document doc = Jsoup.parse(response);
		Elements trResults = doc.getElementsByAttributeValue(CLASS.toString(), SPERESULTS).get(0)
				.getElementsByTag(TBODY).get(0).getElementsByTag(TR.toString());
		trResults.remove(0);
		log.debug("Results count: " + trResults.size());
		return trResults.stream()
				.map(tr -> tr.getElementsByTag(TD.toString()))
				.map(AcrhoUtil::getResult).collect(Collectors.toList());
	}

	/**
	 * Get details for a runner according to his acrho id
	 *
	 * @param id
	 *            The acrho id
	 * @return The details of the runner
	 * @author Vincent Hullaert
	 */
	public AcrhoRunner getRunner(String id) {
		Map<String, String> parameters = AcrhoUtil.getParameters(ap.getRunner());
		parameters.put(CLE_DATA, id);
		String response = post(ap.getRunner(), parameters);
		Document doc = Jsoup.parse(response);
		return AcrhoUtil.getRunner(doc);
	}

	public String getRunType(String runId, String runnerId) {
		Map<String, String> parameters = AcrhoUtil.getParameters(ap.getRunner());
		parameters.put(CLE_DATA, runnerId);
		String response = post(ap.getRunner(), parameters);
		Document doc = Jsoup.parse(response);
		return AcrhoUtil.getType(doc, runId);
	}
	/**
	 *
	 * @param qp The query from yaml file
	 * @param parameters The request parameters cloned
	 * @return The response as {@link String}
	 */
	private String post(QueryProperties qp, Map<String, String> parameters) {
		String response = null;
		try (InputStream is = httpService.post(ap.getBaseUrl() + "/" + qp.getUri(), parameters, ap.getPostRequest().getHeaders())) {
			response = IOUtils.toString(is, ISO_8859_1.name());
		} catch (IOException e) {
			log.error(e);
		}
		log.debug(response);
		return response;
	}

	/**
	 *
	 * @param qp The query from yaml file
	 * @param parameters The request parameters cloned
	 * @return The response as {@link String}
	 */
	private String get(QueryProperties qp, Map<String, String> parameters) {
		String response = null;
		try (InputStream is = httpService.get(ap.getBaseUrl() + "/" + qp.getUri(), parameters)) {
			response = IOUtils.toString(is, ISO_8859_1.name());
		} catch (IOException e) {
			log.error(e);
		}
		log.debug(response);
		return response;
	}
}
