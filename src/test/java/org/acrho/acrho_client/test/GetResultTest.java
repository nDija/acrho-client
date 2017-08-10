package org.acrho.acrho_client.test;

import java.text.ParseException;
import java.util.List;

import org.acrho.acrho_client.ResultDetails;
import org.acrho.acrho_client.RunDetails;
import org.acrho.acrho_client.exception.AcrhoConnectionException;
import org.acrho.acrho_client.exception.AcrhoException;
import org.acrho.acrho_client.service.AcrhoService;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GetResultTest {

	private static final Logger LOG = Logger.getLogger(GetResultTest.class);
	
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
			LOG.debug(run.toString());
		}
	}
	
	@Ignore
	@Test
	public void getRunResultTest() throws AcrhoConnectionException, ParseException {
		List<ResultDetails> results = AcrhoService.getResult(441L);
		Assert.assertNotEquals(0, results.size());
	}
}
