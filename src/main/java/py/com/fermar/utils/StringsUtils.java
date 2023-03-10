package py.com.fermar.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringsUtils {
	
	private StringsUtils() {
	    //Do nothing, S1118 rule
	}
	
    public static String concatenate(String[] array) {
    	StringBuilder bld = new StringBuilder();
        for (String string : array) {
        	bld.append(string);
        }
        return bld.toString();
    }
    
    public static boolean isNullOrEmpty(String string) {
    	return string==null || string.isEmpty();
    }

    /**
     * Método que permite obtener el sha256 de una cadena de texto recibida
     * @param texto cadena de la cual obtener el hash
     * @return hash
     */
    public static String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    texto.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Método que convierte un byte array a hexadecimal
     * @param bytes bytes
     * @return hexadecimal
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
