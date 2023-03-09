package py.com.fermar.utils;

import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

import py.com.fermar.constants.AppConstants;

@PropertySource("file:" + AppConstants.SIFEN_CONFIG_FILENAME)
public class Utils {

	@Value("${sifen.core.baseurl}")
	private String sifenCoreURL;

	public static Map<String, byte[]> getData(String rutaFile, MultipartFile file) {
		HashMap<String, byte[]> data = new HashMap<>();
		File excel = new File(rutaFile);
		
		try (FileInputStream fis = new FileInputStream(excel)){			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for (int readNum; (readNum = fis.read(buffer)) != -1;) {
				bos.write(buffer, 0, readNum);
			}
			byte[] bytesFile = bos.toByteArray();

			data.put("nombre", file.getOriginalFilename().getBytes());
			data.put("file", bytesFile);
		} catch (Exception e) {
			return null;
		}
		return data;
	}

	public static File getFile(Map<String, byte[]> data) throws IOException{
		byte[] bytes = data.get("file");
		String nombre = new String(data.get("nombre"));
		File file = new File(nombre);
		
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(bytes);
			fos.flush();
			return file;
		} 
	}	

	public static String getCleanFecha(String fecha) {
		if (fecha.contains(".")) {
			fecha = fecha.replace(".", "-");
		} else if (fecha.contains("/")) {
			fecha = fecha.replace("/", "-");
		}
		String[] parts = fecha.split("-");
		if (parts[0].length() == 2) {
			fecha = parts[2] + "-" + parts[1] + "-" + parts[0];
		}
		return fecha;
	}

	public static String generateHash(String cadena) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(cadena.getBytes("UTF-8"));
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < digest.length; i++) {
				sb.append(String.format("%02x", digest[i]));
			}
			
			hash = sb.toString();
		} catch (Exception e) {
			return null;
		}
		return hash;
		
	}
	
	/**
	 * Metodo que realiza el calculo del digito verificador de cualquier valor
	 * pasado
	 * 
	 * @param valor
	 *            - valor a calcular
	 * @param baseMax
	 *            - base establecida por defecto como 11
	 * @param digitoDeComparacion
	 *            - digito verificador que se utiliza para comparar con el digito
	 *            verficador gennerado a partir del valor pasado
	 * @return {int} 1,0,-1
	 */
	public static int calcularYComparar(String valor, Integer baseMax, String digitoDeComparacion) {
		int total = 0;
		int resto = 0;
		int k = 2;
		int numeroAux;
		StringBuilder numeroAl = new StringBuilder();
		Integer digitoCalculado = 0;
		// La baseMax tiene como valor por defecto 11
		// cambia la ultima letra por ascii en caso que la cedula termine en letra(caso
		// valido para cedula)
		for (int i = 0; i < valor.length(); i++) {
			String num = valor.substring(i, i + 1).toUpperCase();
			char caracter = num.charAt(0);
			int asciiCaracter = (int) caracter;
			// El numero ascii no debe estar entre 48 y 57
			if (asciiCaracter >= 48 && asciiCaracter <= 57) {
				numeroAl.append(caracter);
			} else {
				numeroAl.append(asciiCaracter);
			}

		}
		for (int i = numeroAl.length(); i > 0; i--) {
			if (k > baseMax) {
				k = 2;
			}
			numeroAux = Integer.parseInt(numeroAl.substring(i - 1, i));
			total = total + (numeroAux * k);
			k++;
		}
		resto = total % 11;
		if (resto > 1) {
			digitoCalculado = 11 - resto;
		} else {
			digitoCalculado = 0;
		}
		
		if (digitoDeComparacion.equals(digitoCalculado.toString())) {
			return 0;
		} else {
			return -1;
		}
	}
	 
	public static boolean isNumericValue(Object value) {
		final Pattern NODIGIT = Pattern.compile("\\D");
		return value != null && !value.toString().isEmpty()
				&& !NODIGIT.matcher(value.toString()).find();
	}
	
	/**
	 * uso exclusivo para test de digito veificador
	 * 
	 * @param valor
	 *            valor
	 * @return digitoCalculado
	 */
	public static String calcularDVExclusivoTest(String valor) {
		int total = 0;
		int resto = 0;
		int k = 2;
		int numeroAux;
		StringBuilder numeroAl = new StringBuilder();
		Integer digitoCalculado = 0;
		// La baseMax tiene como valor por defecto 11
		// cambia la ultima letra por ascii en caso que la cedula termine en letra(caso
		// valido para cedula)
		for (int i = 0; i < valor.length(); i++) {
			String num = valor.substring(i, i + 1).toUpperCase();
			char caracter = num.charAt(0);
			int asciiCaracter = (int) caracter;
			// El numero ascii no debe estar entre 48 y 57
			if (asciiCaracter >= 48 && asciiCaracter <= 57) {
				numeroAl.append(caracter);
			} else {
				numeroAl.append(asciiCaracter);
			}

		}
		for (int i = numeroAl.length(); i > 0; i--) {
			if (k > 11) {
				k = 2;
			}
			numeroAux = Integer.parseInt(numeroAl.substring(i - 1, i));
			total = total + (numeroAux * k);
			k++;
		}
		resto = total % 11;
		if (resto > 1) {
			digitoCalculado = 11 - resto;
		} else {
			digitoCalculado = 0;
		}
		return digitoCalculado.toString();
	}
}
