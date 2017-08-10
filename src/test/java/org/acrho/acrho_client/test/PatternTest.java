package org.acrho.acrho_client.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acrho.acrho_client.IConstants;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the pattern when parsing Runs details
 * @author Vincent Hullaert
 *
 */
public class PatternTest {

	/**
	 * The string to test
	 */
	private static final String RUN = "&nbsp;L HERINNOISE - 19/03/2016 (11.8 km)";
	
	/**
	 * The pattern compiled 
	 */
	private static final Pattern PATTERN = Pattern.compile(IConstants.PATTERN_SELECT_RUN);
	
	/**
	 * The test
	 */
	@Test
	public void selectTest() {
		final Matcher matcher = PATTERN.matcher(RUN);
		final Boolean matches = matcher.matches();
		if(matches) {
			Assert.assertFalse(false);
		} else {
			Assert.assertFalse(true);
		}
	}

}
