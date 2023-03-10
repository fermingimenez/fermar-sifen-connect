package py.com.fermar.exception;

/**
 *
 */
public class ParseXMLException extends RuntimeException {

	private int id;

	public ParseXMLException(String message) {
		super(message);
	}

	public ParseXMLException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public ParseXMLException() {
		super();
	}

	public ParseXMLException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParseXMLException(Throwable cause) {
		super(cause);
	}

	public ParseXMLException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public ParseXMLException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
