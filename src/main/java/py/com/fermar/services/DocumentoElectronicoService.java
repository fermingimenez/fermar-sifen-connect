package py.com.fermar.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;
import py.com.fermar.exception.SIFENException;
import py.com.fermar.exception.SIFENGuardarException;


public interface DocumentoElectronicoService {
	
	Map<String, Object> generarDocumentoElectronico(String xmlFileInJson, Long usuario) throws SIFENException ;
	
	public Long guardarDocumentoElectronicoGratuito(Map<String, Object> parametros, Long usuario) 
			throws SIFENException, TransformerException, 
			IOException, SAXException, ParserConfigurationException, 
			SIFENGuardarException, ParseException;
	
	/**
	 * Obtiene el KuDE en formato PDF
	 * @param cdc
	 * @return
	 * @throws SIFENException
	 */
	byte[] getKuDE(String cdc) throws SIFENException;
	
	int countEventosReceptor(Map<String, Object> parametros);
	
	/**
     * Método para obetener los datos de un documento tributario electrónico por medio de su CDC
     * @param param Body de la petición
     * @return documento tributario electrónico
     */
    ResponseEntity<Object> consultarDE(Map<String, Object> param);

}
