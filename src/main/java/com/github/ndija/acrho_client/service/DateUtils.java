package com.github.ndija.acrho_client.service;

import static com.github.ndija.acrho_client.IConstants.EMPTY_STRING;
import static com.github.ndija.acrho_client.IConstants.H;
import static com.github.ndija.acrho_client.IConstants.M;

import com.github.ndija.acrho_client.IConstants;

public class DateUtils {

	public static Long getMillis(String time) {
		String[] h = time.split(H);
		String[] m = time.split(M);

		Long timeInMillis = 0L;
		if (h.length != 1) {
			timeInMillis = Long.valueOf(h[0]) * 60 * 60 * 1000;
			m = h[1].split(M);
		}

		timeInMillis += Long.valueOf(m[0]) * 60 * 1000;
		timeInMillis += Long.valueOf(m[1].replaceAll(IConstants.S, EMPTY_STRING)) * 1000;
		return timeInMillis;
	}
}
