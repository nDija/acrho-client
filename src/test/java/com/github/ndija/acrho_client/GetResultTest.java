package com.github.ndija.acrho_client;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.exception.AcrhoException;
import com.github.ndija.acrho_client.service.AcrhoService;

public class GetResultTest {

	private static final Logger log = Logger.getLogger(GetResultTest.class);
	
	@Test
	public void getListRunsTest() {
		List<RunDetails> runs;
		try {
			runs = AcrhoService.getRuns();
		} catch (AcrhoException e) {
			Assert.assertFalse(e.getMessage(), true);
			return;
		} catch (AcrhoConnectionException e) {
			Assert.assertFalse(e.getMessage(), true);
			return;
		}
		Assert.assertNotEquals(0, runs.size());
		for (RunDetails run : runs) {
			log.debug(run.toString());
		}
	}
	
	@Test
	public void getRunResultTest() throws AcrhoConnectionException, ParseException {
		List<ResultDetails> results = AcrhoService.getResult(441L);
		Assert.assertNotEquals(0, results.size());
	}
}
