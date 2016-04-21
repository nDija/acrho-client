package org.acrho.test.acrho_client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class PatternTest {

	private static final String PATTERN_SELECT_RUN = "&nbsp;((?:\\w|\\s)*)\\s-\\s(\\d{2}\\/\\d{2}\\/\\d{4})\\s\\((\\d{2}[\\.]*[\\d]*)(?:\\w|\\s)*\\)";
	
	@Test
	public void selectTest() {
		Pattern p = Pattern.compile(PATTERN_SELECT_RUN);
		Matcher m = p.matcher("&nbsp;L HERINNOISE - 19/03/2016 (11.8 km)");
		if(m.matches()) {
			System.out.println("true");
			Assert.assertFalse(false);
		} else {
			Assert.assertFalse(true);
		}
	}

}
