package org.acrho.test.acrho_client;

import java.text.ParseException;

import org.acrho.acrho_client.RunnerDetails;
import org.acrho.acrho_client.exception.AcrhoConnectionException;
import org.acrho.acrho_client.exception.AcrhoException;
import org.acrho.acrho_client.service.AcrhoService;
import org.junit.Assert;
import org.junit.Test;

public class GetRunnerDetailsTest {

	@Test
	public void getRunnerDetailsTest() throws AcrhoConnectionException, ParseException, AcrhoException {
		RunnerDetails runner = AcrhoService.getRunner(1041622622L);
		Assert.assertEquals("Valérian Dupont", runner.getName());
	}
}
