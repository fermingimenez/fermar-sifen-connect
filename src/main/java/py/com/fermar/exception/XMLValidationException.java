package py.com.fermar.exception;

import org.xml.sax.SAXParseException;
import py.gov.sifen.commons.schema.SAXParserErrorHandler;

import java.util.List;

/**
 * Representa a las excepciones lanzadas cuando existe error de validacion de un documento XML
 *
 * @author Luis Fernandez
 * @version 1 06/06/2018
 */
public class XMLValidationException extends RuntimeException {

	private static final String ERROR_MESSAGE = "Error en la linea ?linea, ?columna, error: ?error";

	private int id;
	private List<String> validationsErrors;

	public XMLValidationException(String message) {
		super(message);
	}

	public XMLValidationException(String message, int id) {
		super(message);
		this.setId(id);
	}

	public XMLValidationException() {
		super();
	}

	public XMLValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XMLValidationException(Throwable cause) {
		super(cause);
	}

	public XMLValidationException(Exception ex) {
		super(ex.getMessage(), ex);
	}

	public XMLValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public XMLValidationException(SAXParserErrorHandler saxParserErrorHandler) {
		validationsErrors = saxParserErrorHandler.getExceptionList();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getValidationsErrors() {
		return validationsErrors;
	}

	/**
	 * Construye un mensaje personalizado en base a {@link SAXParseException}
	 *
	 * @param exception Excepcion lanzada por el parseador de XML {@link SAXParseException}
	 * @return Mensaje de error personalizado
	 */
	private String buildCustomErrorValidatorMessage(SAXParseException exception) {
		return ERROR_MESSAGE.replace("?linea", String.valueOf(exception.getLineNumber()))
				.replace("?columna", String.valueOf(exception.getColumnNumber()))
				.replace("?error", exception.getLocalizedMessage());

	}

}
