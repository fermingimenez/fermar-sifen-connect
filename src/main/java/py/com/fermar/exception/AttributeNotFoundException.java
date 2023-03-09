package py.com.fermar.exception;

import java.util.Map;

/**
 * Excepcion utilizada para contemplar los casos en los que no se ha encontrado el atributo dentro de un objeto {@link Map}
 *
 * @author Luis Fernandez
 * @version 0.0.1 25/06/2018
 */
public class AttributeNotFoundException extends RuntimeException {

	private int id;

	public AttributeNotFoundException(String message) {
		super(message);
	}

	public AttributeNotFoundException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public AttributeNotFoundException() {
		super();
	}

	public AttributeNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AttributeNotFoundException(Throwable cause) {
		super(cause);
	}

	public AttributeNotFoundException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public AttributeNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
