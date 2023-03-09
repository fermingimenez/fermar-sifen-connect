package py.com.fermar.exception;

public class MoreOnePropertyFoundException extends RuntimeException {

	private int id;

	public MoreOnePropertyFoundException(String message) {
		super(message);
	}

	public MoreOnePropertyFoundException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public MoreOnePropertyFoundException() {
		super();
	}

	public MoreOnePropertyFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MoreOnePropertyFoundException(Throwable cause) {
		super(cause);
	}

	public MoreOnePropertyFoundException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public MoreOnePropertyFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
