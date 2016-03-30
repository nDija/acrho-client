package com.github.ndija.acrho_client;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.github.ndija.acrho_client.service.AcrhoProperties;
import com.github.ndija.acrho_client.service.HttpService;


public class HttpPostTest {

	public String data = "cle_langue=1011306039&langue=french&cle_menus=1187970092&cle_data=0&adminop=none&ant_order=a.place asc&ant_filter=ant_search_courses&ant_filter_value=441&selected_anchor=0";
	@Test
	public void postTest() throws Exception {
//		String data = FileUtils.readFile("posttest", StandardCharsets.UTF_8);
//		System.out.println(data);
		//"http://www.acrho.org/cust_resultats.php"
		String url = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUN);
		String parameters = AcrhoProperties.get(AcrhoProperties.URL_RESULT_RUN_PARAMETERS);
		InputStream result = HttpService.post(url, data);
		System.out.println(IOUtils.toString(result, StandardCharsets.UTF_8.name()));
	}
}
