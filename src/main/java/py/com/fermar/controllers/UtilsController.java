package py.com.fermar.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import py.com.fermar.exception.SIFENException;
import py.com.fermar.services.UtilsService;

@Controller
@RequestMapping(value = "sifen-utils")
public class UtilsController {

	@Autowired
	private UtilsService utilsService;

    /**
     * Método para obetener los datos de un documento tributario electrónico por medio de su CDC
     * @param param Body de la petición
     * @return documento tributario electrónico
     * @throws SIFENException 
     */
	@PostMapping(value = "firmar")
	public ResponseEntity<Object> firmar(@RequestBody Map<String, Object> param) throws SIFENException {
        return utilsService.firmar(param);
	}


    /**
     * Método para obetener los datos de un documento tributario electrónico y validar el hash generado con
     * una clave secreta del emisor
     * @param param Body de la petición
     * @return documento tributario electrónico
     * @throws SIFENException 
     */
	@PostMapping(value = "calcularQR")
	public ResponseEntity<Object> calcularQR(@RequestBody Map<String, Object> param) throws SIFENException {
        return utilsService.calcularQR(param);
	}
}
