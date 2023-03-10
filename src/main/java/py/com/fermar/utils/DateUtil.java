package py.com.fermar.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {

	private DateUtil() {
		// Do nothing, S1118 - SonarQube Compliant Solution
	}
	
	public static final Long SEGUNDOS_POR_DIA = 86400L;
	protected static final String[] MESES =
			{"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SETIEMBRE",
					"OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};

	public static final String COD_COI_ARG = "ARG";

	public static final String COD_COI_PAR = "PAR";

	public static final int MAYORIA_EDAD_ARG = 18;

	public static final int MAYORIA_EDAD_PAR = 18;

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static Date stringToDate(String date) throws ParseException {
		SimpleDateFormat format = null;
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		
		if(date.indexOf('T') != -1) {
			format = new SimpleDateFormat(DateTimeFormat.FULL_DATETIME_YEAR_TIME_ZONE_SYSTEM.getFormat());
		} else {
			format = new SimpleDateFormat(DateTimeFormat.DATE_YEAR.getFormat());
		}

		return format.parse(date);
	}

	public static Date getDateFromString(String date) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
			if (FormattedDateMatcher.matchFullDateTimeYearTimeZoneSystem(date)) {
				simpleDateFormat
						.applyPattern(DateTimeFormat.FULL_DATETIME_YEAR_TIME_ZONE_SYSTEM.getFormat());
				return simpleDateFormat.parse(date);
			} else if (FormattedDateMatcher.matchFullDateTimeYearWithTimeZone(date)) {
				simpleDateFormat
						.applyPattern(DateTimeFormat.FULL_DATETIME_YEAR_WITH_TIME_ZONE.getFormat());
				return simpleDateFormat.parse(date);
			} else if (FormattedDateMatcher.matchFullDateTimeYear(date)) {
				simpleDateFormat.applyPattern(DateTimeFormat.FULL_DATETIME_YEAR.getFormat());
				return simpleDateFormat.parse(date);
			} else if (FormattedDateMatcher.matchFullDatetime(date)) {
				simpleDateFormat.applyPattern(DateTimeFormat.FULL_DATETIME.getFormat());
				return simpleDateFormat.parse(date);
			} else if (FormattedDateMatcher.matchDateYear(date)) {
				simpleDateFormat.applyPattern(DateTimeFormat.DATE_YEAR.getFormat());
				return simpleDateFormat.parse(date);
			} else if (FormattedDateMatcher.matchDate(date)) {
				simpleDateFormat.applyPattern(DateTimeFormat.DATE.getFormat());
				return simpleDateFormat.parse(date);

			} else if (FormattedDateMatcher.matchAAAAMMDD(date)) {
				simpleDateFormat.applyPattern(DateTimeFormat.AAAAMMDD.getFormat());
				return simpleDateFormat.parse(date);
			} else if (FormattedDateMatcher.matchHour(date)) {
				simpleDateFormat.applyPattern(DateTimeFormat.HOUR.getFormat());
				return simpleDateFormat.parse(date);
			}

		} catch (ParseException e)

		{
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	public static String dateToString(Date date) {
		String fecha = null;
		SimpleDateFormat format = null;
		try {
			if (date == null) {
				return null;
			}

			if (date.toString().indexOf('-') != -1) {
				format = new SimpleDateFormat(DateTimeFormat.FULL_DATETIME_YEAR.getFormat());
			} else {
				format = new SimpleDateFormat(DateTimeFormat.FULL_DATETIME.getFormat());
			}
			fecha = format.format(date);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return fecha;
	}

	public static String dateToStringCustomFormat(Date date, String formato) {
		String fecha = null;
		SimpleDateFormat format = null;
		try {
			if (date == null)
				return null;
			format = new SimpleDateFormat(formato);
			fecha = format.format(date);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return fecha;
	}

	public static Date stringToDateCustomFormat(String date, String formato) {
		Date fecha = null;
		SimpleDateFormat format = null;
		try {
			if (date == null)
				return fecha;
			format = new SimpleDateFormat(formato);
			if (date != "")
				fecha = format.parse(date);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return fecha;
	}

	public static Date stringToTime(String time) {
		Date d1 = null;
		SimpleDateFormat format = null;
		try {
			if (time == null || time.trim().equals("")) {
				return null;
			}
			format = new SimpleDateFormat(DateTimeFormat.HOUR.getFormat());

			d1 = format.parse(time);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return d1;
	}

	public static Date addDays(Date date, int days) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusDays(days).toDate();
	}


	public static int getHoursBetweenDays(String fechaDesdeDDMMYYYY, String fechaHastaDDMMYYYY,
			String horaDesdeHHMM, String horaHastaHHMM) {

		int horas = 0;
		try {
			String fechaDesde = fechaDesdeDDMMYYYY;
			String horaDesde = horaDesdeHHMM;
			String fechaHasta = fechaHastaDDMMYYYY;
			String horaHasta = horaHastaHHMM;

			String format = "ddMMyyyy";

			SimpleDateFormat sdf = new SimpleDateFormat(format);

			Date dateObj1 = sdf.parse(fechaDesde + " " + horaDesde);
			Date dateObj2 = sdf.parse(fechaHasta + " " + horaHasta);

			DateTime dateTime1 = new DateTime(dateObj1);
			DateTime dateTime2 = new DateTime(dateObj2);
			org.joda.time.Period period = new org.joda.time.Period(dateTime1, dateTime2);
			return period.getHours();

		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
		return horas;
	}

	/**
	 * Retorna la cantidad de segundos de diferencia entre dos fechas A y B
	 * dadas.
	 * <p>
	 * No importa el orden de los argumentos, sólo que sean fechas válidas
	 *
	 * @param a Date
	 * @param b Date
	 * @return Long diferencia de segundos entre a y b
	 * @author fgimenez
	 */
	public static Long segundosDiferenciaEntreDates(Date a, Date b) {

		Long diff = 0L;

		if (a.after(b)) {
			diff = (a.getTime() - b.getTime()) / 1000;
		} else if (a.before(b)) {
			diff = (b.getTime() - a.getTime()) / 1000;
		} else if (a.equals(b)) {
			diff = 0L;
		}

		return diff;
	}

	/**
	 * Retorna la cantidad de horas y minutos de diferencia para una duración de
	 * n segundos
	 * <p>
	 * Ejemplo: 12h 2m
	 *
	 * @param segundos segundos
	 * @return diferencia
	 * @author fgimenez
	 */
	public static String horasDiferencia(Long segundos) {
		Duration d = Duration.ofSeconds(segundos);
		Long h = d.toHours();
		Long m = d.minusHours(h).toMinutes();
		return h + "h " + m + "m";
	}

	/**
	 * Retorna la cantidad de segundos de diferencia entre la fecha recibida y
	 * la fecha actual
	 *
	 * @param target fecha
	 * @return Long diferencia de segundos
	 * @author fgimenez
	 */
	public static Long segundosDiferenciaAhora(Date target) {
		Date fechaActual = new Date();
		return (fechaActual.getTime() - target.getTime()) / 1000;
	}

	public static Long minutosDiferenciaAhora(Date target) {
		Date fechaActual = new Date();
		Long d = (fechaActual.getTime() - target.getTime()) / 1000;
		return d / 60;
	}

	public static String getMonthName(int month) {
		if (month < 0 && month > MESES.length + 1) {
			return "ERR";
		}
		return MESES[month - 1];
	}

	public static Date stringToDateTriage(String date) {
		Date d1 = null;
		SimpleDateFormat format = null;
		try {
			if (StringUtils.isEmpty(date)) {
				return null;
			}
			if (date.indexOf('-') != -1) {
				format = new SimpleDateFormat(DateTimeFormat.DATE_YEAR.getFormat());
			} else {
				format = new SimpleDateFormat(DateTimeFormat.DATE.getFormat());
			}
			d1 = format.parse(date);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return d1;
	}

	/**
	 * Recibe un los segundos en long y retorna en horas
	 * @param segundos segundos
	 * @return horas
	 */
	public static String segundosAHoras(Long segundos) {
		String retorno = "";
		int hor; 
		int min; 
		int seg; 
		int segundosInt;

		segundosInt = (int) (segundos / 1);

		hor = (segundosInt / 3600);

		min = (segundosInt - (3600 * hor)) / 60;

		seg = segundosInt - ((hor * 3600) + (min * 60));

		retorno = (hor + "h " + min + "m " + seg + "s");

		return retorno;
	}

	/**
	 * Dado una hora recibida como DATE retorna la fecha recibida y la cantidad
	 * de minutos que transcurrieron desde esa fecha. Se controla si el date
	 * recibido es null o lanza alguna excepción en el proceso.
	 * <p>
	 * Ejemplo de retorno: "2017-06-26 09:47 (288 min)"
	 *
	 * @param date fecha
	 * @return String
	 * @author fgimenez
	 */
	public static String resumenTiempoPretty(Date date) {

		String ret = "";

		if (date != null) {
			try {
				ret = DateUtil.dateToString(date) + " (" + DateUtil.minutosDiferenciaAhora(date)
						+ " min)";
			} catch (Exception e) {
					return null;
			}
		}
		return ret;
	}

	public static boolean isDateBetween(Date fecha, Date fechaDesde, Date fechaHasta) {
		DateTime fechaDT = new DateTime(fecha);
		DateTime fechaDesdeDT = new DateTime(fechaDesde);
		DateTime fechaHastaDT = new DateTime(fechaHasta);
		return fechaDT.isAfter(fechaDesdeDT) || fechaDT.isEqual(fechaDesdeDT) && (
				fechaDT.isBefore(fechaHastaDT) || fechaDT.isEqual(fechaHastaDT));
	}

	/**
	 * Dadas dos fechas recibidas como CALENDAR retorna la cantidad de días de
	 * diferencia que hay entre ambas en Integer
	 * <p>
	 * Ejemplo de retorno: "5"
	 *
	 * @param cal1 Calendar
	 * @param cal2 Calendar
	 * @return String
	 * @author fgimenez
	 */
	public static Integer getDiasEntreCalendars(Calendar cal1, Calendar cal2) {
		long seconds = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000;
		int hours = (int) (seconds / 3600);
		return (hours / 24);
	}

	/**
	 * Dada una fecha y su hora en string, retorna el objeto Date que
	 * corresponde a esa fecha con esa hora
	 *
	 * @param fecha Date
	 * @param hora  String
	 * @return Date
	 * @author fgimenez
	 */
	public static Date getDateWithHour(Date fecha, String hora) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		if (hora != "") {
			String[] horaSplit = hora.split(":");

			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaSplit[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(horaSplit[1]));
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

		}
		return cal.getTime();
	}

	/**
	 * Método que suma un mes a una fecha
	 * @param fecha fecha
	 * @return fecha mas un mes
	 */
	public static Date sumarMes(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}
	
	/**
	 * Método que evalua si una fecha y hora, sumado un valor en una
	 * unidad de tiempo, no supera la fecha y hora actual del sistema. Es decir,
	 * esta dentro del limite.
	 * @param timeToEvaluate
	 * La fecha y hora a evaluar
	 * @param limit
	 * El valor a sumar a la fecha y hora a evaluar
	 * @param unidadTiempo
	 * La unidad de Tiempo (ChronoUnit) que estamos sumando
	 * @return
	 * true: si la fecha y hora sumada con el limite no supera la
	 * fecha y hora del sistema
	 */
	public static boolean fechaHoraPasaLimite(LocalDateTime timeToEvaluate, Integer limit, TemporalUnit unidadTiempo ) {
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime timeTEPlusLimit = timeToEvaluate.plus(limit, unidadTiempo);
				
		return timeTEPlusLimit.isBefore(now);
	}

    public static Timestamp ajustarTimestampAHora(Timestamp ts, LocalTime hora) {
        return Timestamp.valueOf(
                LocalDateTime.of(ts.toLocalDateTime().toLocalDate(), hora)
                        .truncatedTo(ChronoUnit.MILLIS));
    }

    public  static <T extends Comparable<T>> boolean rangosSeSolapan(T inicio1, T fin1, T inicio2, T fin2){
        return (inicio1.compareTo(inicio2) <= 0 && fin1.compareTo(inicio2) >= 0) ||
                (inicio1.compareTo(inicio2) >= 0 && inicio1.compareTo(fin2) <= 0);
    }
}
