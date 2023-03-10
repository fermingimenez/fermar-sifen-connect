package py.com.fermar.utils;

import java.util.Date;

public class XmlBindUtils {


	public static Date parseDateTime(String dateTimeString) {
		return DateUtil.stringToDateCustomFormat(dateTimeString,
				DateTimeFormat.FULL_DATETIME_YEAR_WITH_TIME_ZONE.getFormat());
	}

	public static String printDateTime(Date date) {
		return DateUtil.dateToStringCustomFormat(date,
				DateTimeFormat.FULL_DATETIME_YEAR_WITH_TIME_ZONE.getFormat());
	}

	public static Date parseDateAAAAMMDD(String date) {
		return DateUtil.stringToDateCustomFormat(date,
				DateTimeFormat.AAAAMMDD.getFormat());
	}

	public static String printDateAAAAMMDD(Date date) {
		return DateUtil.dateToStringCustomFormat(date,
				DateTimeFormat.AAAAMMDD.getFormat());
	}

	public static Date parseDateDDMMAAAA(String date) {
		return DateUtil.stringToDateCustomFormat(date,
				DateTimeFormat.DDMMAAAA.getFormat());
	}

	public static String printDateDDMMAAAA(Date date) {
		return DateUtil.dateToStringCustomFormat(date,
				DateTimeFormat.DDMMAAAA.getFormat());
	}

	public static Date parseTime(String timeString) {
		return DateUtil.stringToDateCustomFormat(timeString,
				DateTimeFormat.TIME.getFormat());
	}

	public static String printTime(Date date) {
		return DateUtil.dateToStringCustomFormat(date,
				DateTimeFormat.TIME.getFormat());
	}

	public static Date parseDate(String date) {
		return DateUtil.stringToDateCustomFormat(date,
				DateTimeFormat.DATE_YEAR.getFormat());
	}

	public static String printDate(Date date) {
		return DateUtil.dateToStringCustomFormat(date,
				DateTimeFormat.DATE_YEAR.getFormat());
	}
}
