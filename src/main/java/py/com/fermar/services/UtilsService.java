package py.com.fermar.services;

import org.springframework.http.ResponseEntity;

import py.com.fermar.exception.SIFENException;

import java.util.Map;

public interface UtilsService {

    /**
     * Método para firmar un rDE
     * @param param Body de la petición
     * @return documento rDE firmado
     * @throws SIFENException 
     */
    ResponseEntity<Object> firmar(Map<String, Object> param) throws SIFENException;

    /**
     * Método para obetener el QR de un rDE
     * 
     * @param param Body de la petición
     * @return QR generado
     * @throws SIFENException 
     */
    ResponseEntity<Object> calcularQR(Map<String, Object> param) throws SIFENException;

}
