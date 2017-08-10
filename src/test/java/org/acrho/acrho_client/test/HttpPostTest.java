package org.acrho.acrho_client.test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.acrho.acrho_client.service.AcrhoProperties;
import org.acrho.acrho_client.service.HttpService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;


/**
 * Test if post service respond fine
 * @author Vincent Hullaert
 *
 */
public class HttpPostTest {
	
	@Test
	public void postTest() throws Exception {
		String url = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUN);
		String parameters = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUN_PARAMETERS);
		InputStream result = HttpService.post(url, parameters);
		String sResult = IOUtils.toString(result, StandardCharsets.UTF_8.name());
		Assert.assertTrue("Result is empty", StringUtils.isNotEmpty(sResult));
	}
}
