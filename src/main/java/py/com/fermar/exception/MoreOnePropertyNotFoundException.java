package py.com.fermar.exception;

public class MoreOnePropertyNotFoundException extends MoreOnePropertyFoundException {

	private int id;

	public MoreOnePropertyNotFoundException(String message) {
		super(message);
	}

	public MoreOnePropertyNotFoundException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public MoreOnePropertyNotFoundException() {
		super();
	}

	public MoreOnePropertyNotFoundException(Throwable cause) {
		super(cause);
	}

	public MoreOnePropertyNotFoundException(Exception ex) {
		super(ex.getMessage(), ex);
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
