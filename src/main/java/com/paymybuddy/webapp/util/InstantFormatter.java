package com.paymybuddy.webapp.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class InstantFormatter {

	public InstantFormatter() {

	}

	public String formatDate(Instant instant) {
		Date date = Date.from(instant);
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return formatter.format(date);
	}

}
