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
import org.acrho.client.util.AcrhoUtil;
import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.Headers;


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

@Log4j2
@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {1080})
public class AcrhoServiceTest {

    private AcrhoService as = new AcrhoService();

    private ClientAndServer client;

    private AcrhoProperties acrhoProperties = PropertyService.getInstance().getAcrhoProperties();

    public AcrhoServiceTest(ClientAndServer client) throws IOException{
        this.client = client;
        client.getLocalPort();
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


        Headers headers = new Headers();
        headers.withEntry("User-Agent", "Mozilla/5.0");
        headers.withEntry("Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
        headers.withEntry("Origin", "http://www.acrho.org");
        //Mock for runs HTTP request
        client.when(request().withMethod("GET").withPath("/" + acrhoProperties.getRuns().getUri())
                )
                .respond(response()
                        .withBody("ok"));
        client
                .when(request()
                        .withMethod("POST")
                        .withHeaders(headers)
                        .withPath("/" + acrhoProperties.getRuns().getUri())
                        .withBody(HttpService.buildPostBodyString(runsParameters))
                        )
                .respond(response()
                        .withBody(IOUtils.toString(responseRuns, ISO_8859_1.name())));

        client
                .when(request()
                        .withMethod("POST")
                        .withHeaders(headers)
                        .withPath("/" + acrhoProperties.getResults().getUri())
                        .withBody(HttpService.buildPostBodyString(resultsParameters)))

                .respond(response()
                        .withBody(IOUtils.toString(responseResults, ISO_8859_1.name())));

        client
                .when(request()
                        .withMethod("POST")
                        .withHeaders(headers)
                        .withPath("/" + acrhoProperties.getRunner().getUri())
                        .withBody(HttpService.buildPostBodyString(runnerParameters)))
                //.withQueryStringParameters(TestUtil.toMockServerParameters(runnerParameters)))
                .respond(response()
                        .withBody(IOUtils.toString(responseRunner, ISO_8859_1.name())));
    }

    @Test
    public void getOk() throws IOException{
        String response = new HttpService().get("http://127.0.0.1:1080/" + acrhoProperties.getRuns().getUri(), null);
        assertEquals("ok", response);
    }

    @Test
    //@DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    public void getRuns() throws IOException{
        List<AcrhoRun> runs = as.getRuns("2017");
        runs.forEach(log::debug);
        assertEquals(40, runs.size());
    }

    @Test
    //@DisplayName("When I request an url I get a 200 status codex")
    @ExtendWith(TimingExtension.class)
    public void getResults()  throws IOException{
        as.getRuns("2017");
        List<AcrhoResult> results = as.getResult("2017", "508");
        results.forEach(log::debug);
        assertEquals(831, results.size());
    }

    @Test
    //@DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    public void getRunner()  throws IOException{
        AcrhoRunner runner = as.getRunner("1041624601");
        log.debug(runner);
        assertEquals("Coraline Capelle", runner.getName());
    }

    @Test
    //@DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    public void getRunType()  throws IOException{
        String type = as.getRunType("2356", "1041624601");
        log.debug(type);
        assertEquals("C", type);
    }

    @Test
    //@DisplayName("")
    @ExtendWith(TimingExtension.class)
    public void getResult()  throws IOException{
        as.getResult("2017", "2408");
    }
}
