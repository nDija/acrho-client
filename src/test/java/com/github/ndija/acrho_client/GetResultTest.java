package com.github.ndija.acrho_client;

import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.service.AcrhoService;

public class GetResultTest {

	@Test
	public void getListRunsTest() throws ParseException {
		//http://www.acrho.org/cust_resultats.php?langue=french&cle_menus=1187970092
		List<Run> runs;
		try {
			runs = AcrhoService.getRuns();
		} catch (AcrhoConnectionException e) {
			Assert.assertFalse(e.getMessage(), true);
			return;
		}
		for (Run run : runs) {
			System.out.println(run.toString());
		}
	}
}
