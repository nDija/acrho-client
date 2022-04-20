package io.hullaert.acrho.client;

import java.util.regex.Pattern;

/**
 * interface for static variable
 * @author Vincent Hullaert
 *
 */
public final class AcrhoConstant {

	public static final String PATTERN_SELECT_RUN =
			"&nbsp;((?:\\w|\\s|\\p{L})*)\\s-\\s(\\d{2}\\/\\d{2}\\/\\d{4})\\s\\(([\\d]*[\\.]*[\\d]*)(?:\\w|\\s)*\\)";
	public static final String PATTERN_SELECT_ID_RUNNER = "(?i).*data=(\\d*).*";
	public static final String PATTERN_DATE = "dd/MM/yyyy";
	public static final String PATTERN_TIME = "H'h'mm'm'ss's'";
	public static final String PATTERN_AVG = "m'm'ss's'";
	public static final String STRING_0 = "0";
	public static final String ANT_SEARCH_COURSES = "ant_search_courses";
	public static final String ANT_FILTER_VALUE = "ant_filter_value";
	public static final String CLE_DATA = "cle_data";
	public static final String EMPTY_STRING = "";
	public static final Pattern NBSP_PATTERN = Pattern.compile("&nbsp;");
	public static final String PAR1DESCR = "par1descr";
	public static final String SPEVALUE = "speValue";
	public static final String SPERESULTS = "speResults";
	public static final Pattern S_PATTERN = Pattern.compile("s");
	public static final String M = "m";
	public static final String H = "h";
	public static final Pattern COMA_PATTERN = Pattern.compile(",");
	public static final String POINT = ".";
	public static final String TBODY = "tbody";

	private AcrhoConstant(){}
}
