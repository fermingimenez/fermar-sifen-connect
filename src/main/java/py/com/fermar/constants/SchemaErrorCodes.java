package py.com.fermar.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import py.com.fermar.exception.XmlErrorReport;

/**
 * @author FermínG
 * @version 0.0.1 09/10/2018
 */
public enum SchemaErrorCodes {
	ATRIBUTO_INVALIDO(
			"cvc-attribute\\.3: The value \\'(?<value>[A-Za-z0-9_]+)\\' of attribute \\'(?<attribute>[A-Za-z0-9_]+)\\' on element \\'(?<element>[A-Za-z0-9_]+)\\' is not valid[A-Za-z0-9_\\.\"]*"), 
	VALOR_INVALIDO(
			"cvc-type\\.3\\.1\\.3: The value \\'(?<value>.*?)\\' of element \\'(?<element>[A-Za-z0-9_]+)\\' is not valid[A-Za-z0-9_\\.\"]*"),
	ELEMENTO_INVALIDO(
			"cvc-elt\\.1\\.a: Cannot find the declaration of element \\'(?<element>[A-Za-z0-9_]+)\\'*"), 
	CONTENIDO_INVALIDO(
			"cvc-complex-type\\.2\\.4\\.a: Invalid content was found starting with element \\'(?<element>[A-Za-z0-9\\.\\/\\\"]+)\\'\\. One of \\'\\{\\\"(?<expected>[A-Za-z0-9\\.\\/\\:\"]+)\\}\\' is expected[A-Za-z0-9\\.\\/\\:\"]*"), 
	ELEMENTO_FALTANTE(
			 "cvc-complex-type\\.2\\.4\\.a: Invalid content was found starting with element \'(?<element>[A-Za-z0-9\\.\\/\"]+)\'\\. One of \'\\{\".*?\"\\:(?<expected>[A-Za-z0-9]+).*?\\}\'"),  
	ELEMENTO_FALTANTE_GRUPO(
			"cvc-complex-type\\.2\\.4\\.d: Invalid content was found starting with element \\'(?<element>[A-Za-z0-9\\.\\/\\\"]+)\\'\\. No child element is expected .*?"),
	GRUPO_FALTANTE(
			"cvc-complex-type\\.2\\.4\\.b: The content of element \'(?<group>[A-Za-z0-9\\.\\/\"]+)\' .*? One of \'\\{\".*?\"\\:(?<expected>[A-Za-z0-9]+)\\}\'.*?");
	
	private String regexErrorCode;

	SchemaErrorCodes(String regexErrorCode) {
		this.regexErrorCode = regexErrorCode;
	}

	public String getRegexErrorCode() {
		return regexErrorCode;
	}

	/**
	 *Reglas que se excluyen porque ya se validan con el pattern en 
	 *SchemaErrorCodes.regexErrorCode
	 */
	private static final String EXCLUDED_RULE_XSD = "cvc-maxLength-valid:.*?|"
													+ "cvc-minLength-valid:.*?|"
													+ "cvc-datatype-valid.1.2.1:.*?|"
													+ "cvc-pattern-valid:.*?|"
													//+ "cvc-complex-type.2.4.b:.*?|"
													+ "cvc-enumeration-valid:.*?|"
													+ "cvc-length-valid:.*?|"
													+ "cvc-minInclusive-valid:.*?|"
													+ "cvc-maxInclusive-valid:.*?";
	
	/**
	 *
	 * @param validationErrorMessage mensaje de error de validación
	 * @return reporte xml
	 */
	public static XmlErrorReport parseErrorMessage(String validationErrorMessage) {
		XmlErrorReport error = new XmlErrorReport();
		
		for (SchemaErrorCodes schemaErrorCodes : SchemaErrorCodes.values()) {
			Pattern pattern = Pattern.compile(schemaErrorCodes.getRegexErrorCode());
			Matcher matcher = pattern.matcher(validationErrorMessage);
			if (matcher.find()) {
				error.setValue(getGroup(matcher, "value"));
				error.setElement(getGroup(matcher, "element"));
				error.setExpected(getGroup(matcher, "expected"));
				error.setGroup(getGroup(matcher, "group"));
			}
		}
		
		if(error.getValue() ==null &&
				!Pattern.matches(EXCLUDED_RULE_XSD, validationErrorMessage)) {
			error.setValue(validationErrorMessage);
			error.setElement("N/D");			
		}
		
		return error;
	}

	/**
	 *
	 * @param matcher matcher
	 * @param group grupo
	 * @return grupo matcheado
	 */
	private static String getGroup(Matcher matcher, String group) {
		String groupValue = "";
		try {
			groupValue = matcher.group(group);
			return groupValue;
		} catch (Exception e) {
			// No hacer nada
		}
		return groupValue;
	}

}
