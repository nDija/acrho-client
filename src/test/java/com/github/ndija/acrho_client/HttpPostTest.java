package com.github.ndija.acrho_client;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.github.ndija.acrho_client.service.AcrhoProperties;
import com.github.ndija.acrho_client.service.HttpService;
import com.github.ndija.acrho_client.service.IAcrhoProperties;


public class HttpPostTest {

	private static final Logger log = Logger.getLogger(HttpPostTest.class);
	
	public String data = "cle_langue=1011306039&langue=french&cle_menus=1187970092&cle_data=0&adminop=none&ant_order=a.place asc&ant_filter=ant_search_courses&ant_filter_value=441&selected_anchor=0";
	
	@Test
	public void postTest() throws Exception {
		String url = AcrhoProperties.get(IAcrhoProperties.URL_RESULT_RUN);
		String parameters = AcrhoProperties.get(IAcrhoProperties.URL_RESULT_RUN_PARAMETERS);
		InputStream result = HttpService.post(url, parameters);
		log.debug(IOUtils.toString(result, StandardCharsets.UTF_8.name()));
	}
}
