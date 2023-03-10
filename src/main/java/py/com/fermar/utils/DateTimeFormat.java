package py.com.fermar.utils;

public enum DateTimeFormat {
	FULL_DATETIME("dd-MM-yyyy HH:mm"),
	FULL_DATETIME_YEAR("yyyy-MM-dd HH:mm"),
	FULL_DATETIME_YEAR_WITH_TIME_ZONE("yyyy-MM-dd'T'HH:mm:ssXXX"),
	FULL_DATETIME_YEAR_TIME_ZONE_SYSTEM("yyyy-MM-dd'T'HH:mm:ss"),
	DATE_YEAR("yyyy-MM-dd"),
	DATE("dd/MM/yyyy"),
	HOUR("HH:mm"),
	TIME("HH:mm:ss"),
	AAAAMMDD ("yyyyMMdd"),
	DDMMAAAA ("dd-MM-yyyy");


	private String format;

	DateTimeFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return this.format;
	}
}
