package py.com.fermar.controllers;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import py.com.fermar.authproviders.IAuthenticationFacade;
import py.com.fermar.exception.SIFENException;
import py.com.fermar.services.DocumentoElectronicoService;
import py.com.fermar.services.UtilsService;

@Controller
@RequestMapping(value = "sifen-connect")
public class SifenController {

	@Autowired
	private UtilsService utilsService;

	@Autowired
	private DocumentoElectronicoService documentosElectronicosGratuitoService;
	
	@Autowired
	IAuthenticationFacade authenticationFacade;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SifenController.class.getName());


	private static final String FILTROS = "filtros";
	private static final String CDC_INVALIDO = "CDC inválido";
	private static final String ID = "id";
	public static final String MENSAJE_ERROR = "Error Inesperado";
	public static final String OPERACION_EVENTO = "ekuatiai-evento";
	private static final String MENSAJE = "mensaje";
	public static final String PROCESS_ID = "controller";
	
	
	@PostMapping(value = "generar-de")
	public ResponseEntity<Object> generarProcesoFirma(@RequestBody String param) {
		Map<String, Object> mapDTEResponse = new HashMap<>();

		try {

			mapDTEResponse = documentosElectronicosGratuitoService.generarDocumentoElectronico(param,
					authenticationFacade.getUsuarioId());
			mapDTEResponse.put(MENSAJE, "Proceso Generado");
			return new ResponseEntity<>(mapDTEResponse, HttpStatus.ACCEPTED);

		} catch (SIFENException xsnfe) {
			mapDTEResponse.put(MENSAJE, xsnfe.getMessage());
			LOGGER.error(PROCESS_ID + " : " + xsnfe.getMessage(), xsnfe);
			return ResponseEntity.status(HttpStatus.OK).body(mapDTEResponse);

		} catch (Exception e) {
			mapDTEResponse.put(MENSAJE, MENSAJE_ERROR);
			LOGGER.error(PROCESS_ID + " : " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.OK).body(mapDTEResponse);

		}
	}
	

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
