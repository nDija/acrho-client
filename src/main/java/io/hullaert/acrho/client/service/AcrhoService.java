package io.hullaert.acrho.client.service;

import io.hullaert.acrho.client.model.property.AcrhoProperties;
import io.hullaert.acrho.client.model.property.QueryProperties;
import io.hullaert.acrho.client.AcrhoClientException;
import io.hullaert.acrho.client.model.AcrhoResult;
import io.hullaert.acrho.client.model.AcrhoRun;
import io.hullaert.acrho.client.model.AcrhoRunner;
import io.hullaert.acrho.client.util.AcrhoUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.swing.text.html.HTML.Attribute.*;
import static javax.swing.text.html.HTML.Tag.*;
import static io.hullaert.acrho.client.AcrhoConstant.*;

/**
 * Main service class for retrieving information from the site acrho.org
 *
 * @author Vincent Hullaert
 *
 */
public class AcrhoService {

	private static final Logger LOG = LoggerFactory.getLogger(AcrhoService.class);

	private final AcrhoProperties ap = PropertyService.getInstance().getAcrhoProperties();

	/**
	 * Get a list of runs by year
	 *
	 * @param year The year of the runs
	 * @return List of runs of the current year
	 * @author Vincent Hullaert
	 */
	public List<AcrhoRun> getRuns(String year) throws AcrhoClientException {
		var parameters = AcrhoUtil.getParameters(ap.getRuns());
		parameters.put(ANT_FILTER_VALUE, year);
		var response = get(ap.getRuns(), parameters);
		var options = Jsoup.parse(response)
				.getElementsByAttributeValue(NAME.toString(), ANT_SEARCH_COURSES).get(0)
				.getElementsByTag(OPTION.toString());
		if(LOG.isDebugEnabled())
			LOG.debug(MessageFormat.format("Found courses: {0}", (options.size() - 1)));
		return options.stream()
				.filter(option -> !option.attr(VALUE.toString()).equals(STRING_0))
				.map(AcrhoUtil::getRun)
				.toList();
	}

	/**
	 * Get a list of results by run id
	 *
	 * @param id
	 *            The id of the run
	 * @return List of results for a run
	 * @author Vincent Hullaert
	 */
	public List<AcrhoResult> getResult(String id) throws AcrhoClientException {

		var parameters = AcrhoUtil.getParameters(ap.getResults());
		parameters.put(ANT_FILTER_VALUE, id);
		var response = post(ap.getResults(), parameters);
		var doc = Jsoup.parse(response);
		var trResults = doc.getElementsByAttributeValue(CLASS.toString(), SPERESULTS).get(0)
				.getElementsByTag(TBODY).get(0).getElementsByTag(TR.toString());
		trResults.remove(0);
		if(LOG.isDebugEnabled())
			LOG.debug(MessageFormat.format("Results count: {0}", trResults.size()));
		return trResults.stream()
				.map(tr -> tr.getElementsByTag(TD.toString()))
				.map(AcrhoUtil::getResult).toList();
	}

	/**
	 * Get details for a runner according to his acrho id
	 *
	 * @param id
	 *            The acrho id
	 * @return The details of the runner
	 * @author Vincent Hullaert
	 */
	public AcrhoRunner getRunner(String id) throws AcrhoClientException {
		var parameters = AcrhoUtil.getParameters(ap.getRunner());
		parameters.put(CLE_DATA, id);
		var response = post(ap.getRunner(), parameters);
		var doc = Jsoup.parse(response);
		return AcrhoUtil.getRunner(doc);
	}

	public String getRunType(String runId, String runnerId) throws AcrhoClientException {
		Map<String, String> parameters = AcrhoUtil.getParameters(ap.getRunner());
		parameters.put(CLE_DATA, runnerId);
		String response = post(ap.getRunner(), parameters);
		Document doc = Jsoup.parse(response);
		return AcrhoUtil.getType(doc, runId);
	}

	private String get(QueryProperties qp, Map<String, String> parameters)
			throws AcrhoClientException {

		List<NameValuePair> nvp = new ArrayList<>();
		parameters.forEach((k,v) -> nvp.add(new BasicNameValuePair(k,v)));

		return AcrhoHttpClient.get(
				ap.getBaseUrl() + "/" + qp.getUri(),
				ap.getGetRequest().getHeaders(),
				nvp, ISO_8859_1
		);
	}

	/**
	 *
	 * @param qp The query from yaml file
	 * @return The response as {@link String}
	 */
	private String post(QueryProperties qp, Map<String, String> body)
			throws AcrhoClientException {

		return AcrhoHttpClient.post(
				ap.getBaseUrl() + "/" + qp.getUri(),
				ap.getPostRequest().getHeaders(),
				null, body,
				ISO_8859_1);
	}
}
