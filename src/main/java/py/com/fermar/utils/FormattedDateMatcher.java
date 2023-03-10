package py.com.fermar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class FormattedDateMatcher {


	private FormattedDateMatcher() {
		// Do nothing, SonarQube (S1118) Compliant Solution
	}
	
	public static boolean matchFullDateTimeYearWithTimeZone(String date){
		return matchPatter(date, DateTimeFormat.FULL_DATETIME_YEAR_WITH_TIME_ZONE.getFormat());
	}
	
	public static boolean matchFullDateTimeYearTimeZoneSystem(String date){
		return matchPatter(date, DateTimeFormat.FULL_DATETIME_YEAR_TIME_ZONE_SYSTEM.getFormat());
	}

	public static boolean matchFullDatetime(String date) {
		return matchPatter(date, DateTimeFormat.FULL_DATETIME.getFormat());
	}

	public static boolean matchFullDateTimeYear(String date) {
		return matchPatter(date, DateTimeFormat.FULL_DATETIME_YEAR.getFormat());
	}

	public static boolean matchDateYear(String date) {
		return matchPatter(date, DateTimeFormat.DATE_YEAR.getFormat());
	}

	public static boolean matchDate(String date) {
		return matchPatter(date, DateTimeFormat.DATE.getFormat());
	}

	public static boolean matchHour(String date) {
		return matchPatter(date, DateTimeFormat.HOUR.getFormat());
	}

	public static boolean matchAAAAMMDD(String date) {
		return matchPatter(date, DateTimeFormat.AAAAMMDD.getFormat());
	}

	public static boolean matchPatter(String date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			dateFormat.parse(date.trim());
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	
	private static final Pattern DATE_GREGORIAN_PATTERN_AAAAMMDD = Pattern.compile(
		      "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
		      + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
		      + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
		      + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
		 
  /**
   * Valida que una fecha string sea válida en formato AAAA-MM-DD
   * @param date
   * @return boolean
   */
	public static boolean isValidPatternAAAAMMDD(String date) {
      return DATE_GREGORIAN_PATTERN_AAAAMMDD.matcher(date).matches();
  }
}
