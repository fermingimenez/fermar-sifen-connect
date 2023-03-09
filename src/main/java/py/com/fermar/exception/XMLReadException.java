package py.com.fermar.exception;

public class XMLReadException extends RuntimeException {

	private static final String ERROR_MESSAGE = "Error al leer el XML de origen";
	private int id;

	public XMLReadException(String message) {
		super(message);
	}

	public XMLReadException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public XMLReadException() {
		super(ERROR_MESSAGE);
	}

	public XMLReadException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XMLReadException(Throwable cause) {
		super(cause);
	}

	public XMLReadException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public XMLReadException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
