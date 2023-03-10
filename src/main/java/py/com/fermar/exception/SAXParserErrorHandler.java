package py.com.fermar.exception;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import py.com.fermar.constants.SchemaErrorCodes;
import py.com.fermar.utils.StringsUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luis Fernandez
 * @version 1 07/06/2018
 */
public class SAXParserErrorHandler implements ErrorHandler {

	private static final String ERROR_MESSAGE =
			"El valor ?valor del elemento: ?elemento es invalido";

	private static final String ERROR_MESSAGE_EXPECTED =
			"El elemento esperado es: ?expected en lugar de: ?elemento";

	private static final String ERROR_MESSAGE_EXPECTED_IN_GROUP =
			"Elemento esperado: ?expected dentro de: ?group";

	private List<String> messagesExceptionList = new ArrayList<>();

	@Override
	public void warning(SAXParseException exception) {
		addExceptionMessage(exception);
	}

	@Override
	public void error(SAXParseException exception) {
		addExceptionMessage(exception);
	}

	@Override
	public void fatalError(SAXParseException exception) {
		addExceptionMessage(exception);
	}

	private void addExceptionMessage(SAXParseException exception) {
		String messageEx = buildExceptionMessage(exception);
		if (messageEx != null) {
			messagesExceptionList.add(messageEx);
		}
	}

	public List<String> getExceptionList() {
		return messagesExceptionList;
	}

	private String buildExceptionMessage(SAXParseException exception) {

		XmlErrorReport errorReport =
				SchemaErrorCodes.parseErrorMessage(exception.getLocalizedMessage());

		if (errorReport.getValue() == null 
				&& errorReport.getElement() == null
				&& errorReport.getExpected() == null) {
			return null;
		}

		if (!StringsUtils.isNullOrEmpty(errorReport.getExpected())
				&& !StringsUtils.isNullOrEmpty(errorReport.getGroup())) {
			return ERROR_MESSAGE_EXPECTED_IN_GROUP
					.replace("?expected",
							errorReport.getExpected() != null ? errorReport.getExpected() : "")
					.replace("?group",
							errorReport.getGroup() != null ? errorReport.getGroup() : "");
		}

		if (!StringsUtils.isNullOrEmpty(errorReport.getExpected())) {
			return ERROR_MESSAGE_EXPECTED
					.replace("?expected",
							errorReport.getExpected() != null ? errorReport.getExpected() : "")
					.replace("?elemento",
							errorReport.getElement() != null ? errorReport.getElement() : "");
		}

		return ERROR_MESSAGE
				.replace("?valor", errorReport.getValue() != null ? errorReport.getValue() : "")
				.replace("?elemento",
						errorReport.getElement() != null ? errorReport.getElement() : "");

	}
}
