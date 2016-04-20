package com.github.ndija.acrho_client;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.exception.AcrhoException;
import com.github.ndija.acrho_client.service.AcrhoService;

public class GetRunnerDetailsTest {

	@Test
	public void getRunnerDetailsTest() throws AcrhoConnectionException, ParseException, AcrhoException {
		RunnerDetails runner = AcrhoService.getRunner(1041622622L);
		Assert.assertEquals("Valérian Dupont", runner.getName());
	}
}
