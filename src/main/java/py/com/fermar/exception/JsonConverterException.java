package py.com.fermar.exception;

import java.util.Map;

/**
 * Excepcion utilizada para contemplar los casos en los que ha ocurrido un error durante la conversion de XML a JSON
 *
 * @author Luis Fernandez
 * @version 0.0.1 25/06/2018
 */
public class JsonConverterException extends RuntimeException {

	private static final String ERROR_MSG =
			"Ha ocurrido un error durante el proceso de conversion de XML a JSON";

	private int id;

	public JsonConverterException(String message) {
		super(message);
	}

	public JsonConverterException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public JsonConverterException() {
		super();
	}

	public JsonConverterException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JsonConverterException(Throwable cause) {
		super(ERROR_MSG, cause);
	}

	public JsonConverterException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public JsonConverterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
