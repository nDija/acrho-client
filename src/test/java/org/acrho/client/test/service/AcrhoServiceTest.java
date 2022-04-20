package org.acrho.client.test.service;

import org.acrho.client.AcrhoClientException;
import org.acrho.client.model.AcrhoResult;
import org.acrho.client.model.AcrhoRun;
import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.model.property.AcrhoProperties;
import org.acrho.client.service.AcrhoService;
import org.acrho.client.service.HttpService;
import org.acrho.client.service.PropertyService;
import org.acrho.client.test.TimingExtension;
import org.acrho.client.util.AcrhoUtil;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.acrho.client.AcrhoConstant.ANT_FILTER_VALUE;
import static org.acrho.client.AcrhoConstant.CLE_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@MockServerSettings(ports = {1080})
@ExtendWith(MockServerExtension.class)
class AcrhoServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(AcrhoServiceTest.class);

    private final AcrhoService as = new AcrhoService();

    private final ClientAndServer client;

    private static final AcrhoProperties acrhoProperties = PropertyService.getInstance().getAcrhoProperties();

    public AcrhoServiceTest(ClientAndServer client) {
        this.client = client;
    }

    @BeforeAll
    public static void initMockServer(ClientAndServer client) throws IOException{
        InputStream responseRuns = AcrhoServiceTest.class.getClassLoader()
                .getResourceAsStream("acrho/acrho_runs_2017.html");
        InputStream responseResults = AcrhoServiceTest.class.getClassLoader()
                .getResourceAsStream("acrho/acrho_results_508.html");
        InputStream responseRunner = AcrhoServiceTest.class.getClassLoader()
                .getResourceAsStream("acrho/acrho_runner_1041624601.html");
        Map<String, String> runsParameters = AcrhoUtil.getParameters(acrhoProperties.getRuns());
        runsParameters.put(ANT_FILTER_VALUE, "2017");
        Map<String, String> resultsParameters = AcrhoUtil.getParameters(acrhoProperties.getResults());
        resultsParameters.put(ANT_FILTER_VALUE, "508");
        Map<String, String> runnerParameters = AcrhoUtil.getParameters(acrhoProperties.getRunner());
        runnerParameters.put(CLE_DATA, "1041624601");

        //Mock for runs HTTP request
        assert responseRuns != null;
        client
                .when(request()
                        .withMethod("POST")
                        .withPath("/" + acrhoProperties.getRuns().getUri())
                        .withBody(HttpService.buildPostBodyString(runsParameters)))
                .respond(response()
                        .withBody(IOUtils.toString(responseRuns, ISO_8859_1.name())));

        assert responseResults != null;
        client
                .when(request()
                        .withMethod("POST")
                        .withPath("/" + acrhoProperties.getResults().getUri())
                        .withBody(HttpService.buildPostBodyString(resultsParameters)))
                .respond(response()
                        .withBody(IOUtils.toString(responseResults, ISO_8859_1.name())));

        assert responseRunner != null;
        client
                .when(request()
                        .withMethod("POST")
                        .withPath("/" + acrhoProperties.getRunner().getUri())
                        .withBody(HttpService.buildPostBodyString(runnerParameters)))
                        //.withQueryStringParameters(TestUtil.toMockServerParameters(runnerParameters)))
                .respond(response()
                        .withBody(IOUtils.toString(responseRunner, ISO_8859_1.name())));
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetSessionId() throws AcrhoClientException {
        List<AcrhoRun> runs = as.getRuns("2017");
        runs.forEach(r -> LOG.debug(r.toString()));
        assertEquals(40, runs.size(), "Test if number of runs is equals as expected");
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetRuns() throws AcrhoClientException {
        List<AcrhoRun> runs = as.getRuns("2017");
        runs.forEach(r -> LOG.debug(r.toString()));
        assertEquals(40, runs.size(), "Test if number of runs is equals as expected");
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetResults() throws AcrhoClientException {
        as.getRuns("2017");
        List<AcrhoResult> results = as.getResult("508");
        results.forEach(r -> LOG.debug(r.toString()));
        assertEquals(831, results.size(), "");
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetRunner() throws AcrhoClientException {
        AcrhoRunner runner = as.getRunner("1041624601");
        LOG.debug(runner.toString());
        assertEquals("Coraline Capelle", runner.getName(), "");
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetRunType() throws AcrhoClientException {
        String type = as.getRunType("2356", "1041624601");
        LOG.debug(type);
        assertEquals("C", type, "");
    }
}
