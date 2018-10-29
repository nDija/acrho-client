package org.acrho.client.test.service;

import lombok.extern.log4j.Log4j2;
import org.acrho.client.model.AcrhoResult;
import org.acrho.client.model.AcrhoRun;
import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.model.property.AcrhoProperties;
import org.acrho.client.service.AcrhoService;
import org.acrho.client.service.HttpService;
import org.acrho.client.service.PropertyService;
import org.acrho.client.test.TimingExtension;
import org.acrho.client.test.util.TestUtil;
import org.acrho.client.util.AcrhoUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.acrho.client.AcrhoConstant.ANT_FILTER_VALUE;
import static org.acrho.client.AcrhoConstant.CLE_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Log4j2
public class AcrhoServiceTest {

    private AcrhoService as = new AcrhoService();

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 1080);

    private MockServerClient mockServerClient = null;

    private AcrhoProperties acrhoProperties = PropertyService.getInstance().getAcrhoProperties();

    @Before
    public void initMockServer() throws IOException{
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
        mockServerClient
                .when(request()
                        .withMethod("POST")
                        .withPath("/" + acrhoProperties.getRuns().getUri())
                        .withBody(HttpService.buildPostBodyString(runsParameters)))
                .respond(response()
                        .withBody(IOUtils.toString(responseRuns, ISO_8859_1.name())));

        mockServerClient
                .when(request()
                        .withMethod("POST")
                        .withPath("/" + acrhoProperties.getResults().getUri())
                        .withBody(HttpService.buildPostBodyString(resultsParameters)))
                .respond(response()
                        .withBody(IOUtils.toString(responseResults, ISO_8859_1.name())));

        mockServerClient
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
    public void getRuns() {
        List<AcrhoRun> runs = as.getRuns("2017");
        runs.forEach(log::debug);
        assertEquals(40, runs.size());
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    public void getResults() {
        as.getRuns("2017");
        List<AcrhoResult> results = as.getResult("508");
        results.forEach(log::debug);
        assertEquals(831, results.size());
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    public void getRunner() {
        AcrhoRunner runner = as.getRunner("1041624601");
        log.debug(runner);
        assertEquals("Coraline Capelle", runner.getName());
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    public void getRunType() {
        String type = as.getRunType("2356", "1041624601");
        log.debug(type);
        assertEquals("C", type);
    }
}
