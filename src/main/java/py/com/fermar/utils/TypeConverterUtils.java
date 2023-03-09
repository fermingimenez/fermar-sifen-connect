package py.com.fermar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Clase utilitaria para la conversion de tipos
 */
public class TypeConverterUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class.getName());


	private TypeConverterUtils() {
		// Do nothing, SonarQube (S1118) Compliant Solution
	}
	
	public static <T, S> S convert(T data, Class<S> clazzToConvert) {
		try {
			if (data instanceof Number) {
				return clazzToConvert
						.cast(convertNumber(data, clazzToConvert.asSubclass(Number.class)));
			} else if (data instanceof String) {
				if (Date.class.isAssignableFrom(clazzToConvert)) {
					return clazzToConvert.cast(convertDate(data.toString()));
				} else if (Number.class.isAssignableFrom(clazzToConvert)) {
					return convertStringToNumber(data.toString(), clazzToConvert);
				} else if (String.class.isAssignableFrom(clazzToConvert)) {
					return clazzToConvert.cast(data.toString());
				}
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("No es posible convertir: {} a {} ", data.getClass(),
					clazzToConvert.getClass());
			throw new UnsupportedOperationException(
					"No es posible convertir:" + data.getClass() + " a " + clazzToConvert
							.getClass());
		}
	}

	private static <T> T convertStringToNumber(String data, Class<T> clazzToConvert) {
		if (Integer.class.isAssignableFrom(clazzToConvert)) {
			return clazzToConvert.cast(Integer.valueOf(data));
		} else if (Long.class.isAssignableFrom(clazzToConvert)) {
			return clazzToConvert.cast(Long.valueOf(data));
		} else if (Double.class.isAssignableFrom(clazzToConvert)) {
			return clazzToConvert.cast(Double.valueOf(data));
		} else if (BigDecimal.class.isAssignableFrom(clazzToConvert)) {
			return clazzToConvert.cast(new BigDecimal(data));
		}
		throw new UnsupportedOperationException(
				"No es posible convertir:" + data.getClass() + " a " + clazzToConvert.getClass());
	}

	/**
	 * @param data data
	 * @param clazzToConvert class converter
	 * @return numero convertido
	 */
	private static <T, S extends Number> S convertNumber(T data, Class<S> clazzToConvert) {
		Number dataNumber = Number.class.cast(data);
		if (clazzToConvert.isAssignableFrom(Integer.class)) {
			return clazzToConvert.cast(dataNumber.intValue());
		} else if (clazzToConvert.isAssignableFrom(Long.class)) {
			return clazzToConvert.cast(dataNumber.longValue());
		} else if (clazzToConvert.isAssignableFrom(Double.class)) {
			return clazzToConvert.cast(dataNumber.doubleValue());
		} else if (clazzToConvert.isAssignableFrom(BigDecimal.class)) {
			return clazzToConvert.cast(new BigDecimal(data.toString()));
		} else {
			return clazzToConvert.cast(dataNumber);
		}

	}


	/**
	 * @param data fecha
	 * @return fecha convertida
	 */
	private static Date convertDate(String data) {
		return DateUtil.getDateFromString(data);
	}

	/**
	 * Convierte un {@link String} a un objeto del tipo pasado como parametro
	 *
	 * @param data           Dato a convertir
	 * @param classToConvert Clase a la que se debe convertir
	 * @param classOfData Clase de la que se desea convertir
	 * @param <T> tipo de dato
	 * @return Por defecto retorna el mismo dato del tipo string, caso contrario convierte al tipo segun lo codificado
	 */
	public static <T> T convertString(String data, Class<T> classToConvert, Class classOfData) {
		return (T) data;
	}
}
