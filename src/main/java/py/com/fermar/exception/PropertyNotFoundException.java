package py.com.fermar.exception;

import java.util.Map;

/**
 * Excepcion utilizada para contemplar los casos en los que no se ha encontrado una propiedad dentro de un objeto {@link Map}
 *
 * @author Luis Fernandez
 * @version 0.0.1 25/06/2018
 */
public class PropertyNotFoundException extends RuntimeException {

	private int id;

	public PropertyNotFoundException(String message) {
		super(message);
	}

	public PropertyNotFoundException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public PropertyNotFoundException() {
		super();
	}

	public PropertyNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PropertyNotFoundException(Throwable cause) {
		super(cause);
	}

	public PropertyNotFoundException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public PropertyNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
