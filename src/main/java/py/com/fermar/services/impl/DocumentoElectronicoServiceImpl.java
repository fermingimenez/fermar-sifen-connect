package py.com.fermar.services.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.bind.JAXB;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.codec.digest.DigestUtils;
//import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import py.com.fermar.dto.ContribuyenteDTO;
import py.com.fermar.exception.AttributeNotFoundException;
import py.com.fermar.exception.SIFENException;
import py.com.fermar.exception.SIFENGuardarException;
import py.com.fermar.exception.XMLReadException;
import py.com.fermar.request.RDE;
import py.com.fermar.request.TgCamItem;
import py.com.fermar.request.TDE;
import py.com.fermar.request.TgActEco;
import py.com.fermar.services.DocumentoElectronicoService;
import py.com.fermar.services.UtilsService;
import py.com.fermar.utils.EmailUtils;
import py.com.fermar.utils.JasperUtils;
import py.com.fermar.utils.Utils;
import py.com.fermar.utils.XmlUtils;

@Service
public class DocumentoElectronicoServiceImpl implements DocumentoElectronicoService{
	
	
	@Autowired
	private String sifenUrlConsultaQr;
	
	/*@Autowired
	private DocumentoElectronicoXmlService documentoElectronicoXmlService;
	
	@Autowired
	private DocumentosElectronicoService documentosElectronicoService;*/

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private DataSource dataSource;    
	    
    @Autowired
    private String validacionRucCertificado;
       
        
    private static final String RUTA_VERSION = "rDE.dVerFor";
    private static final String DIGEST = "DigestValue";
    private static final String DIGEST_PREFIX = "dsig:DigestValue";
    private static final String QR = "dCarQR";
    private static final String CDC = "cdc";
    private static final String RUTA_DE = "rDE.DE";
    private static final String DTE = "DTE";   
    private static final String EVENTO = "Evento";
    private static final String DTIGDE = "dTiGDE";
    private static final String DPROTAUT = "dProtAut";
    private static final String DFECPROC = "dFecProc";
    private static final String DESTRES = "dEstRes";
    private static final String APROBADO = "Aprobado";    
    private static final String QRCODE_KEY = "qrCode";
    private static final String DVERFOR_KEY ="dVerFor";
        
	private static final String ID = "Id";
	
	private static final String DTOTGRALOPE_TAG = "dTotGralOpe";			
    
	private static final String ERROR_MESSAGE = "Error al obtener: ";
	
	private static final String TIPO_DOCUMENTO = "iTiDE";
	private static final String RUC_EMISOR = "dRucEm";
	private static final String DIGITO_VERIFICADOR = "dDVEmi";
	private static final String ESTABLECIMIENTO = "dEst";
	private static final String PUNTO_EXPEDICION = "dPunExp";
	private static final String NRO_DOCUMENTO = "dNumDoc";
	private static final String TIPO_CONTRIBUYENTE = "iTipCont";
	private static final String FECHA_EMISION = "dFeEmiDE";
	private static final String FECHA_FIRMA = "dFecFirma";
	private static final String TIPO_EMISION = "iTipEmi";
	private static final String CODIGO_SEGURIDAD = "dCodSeg";
	private static final String RDE = "rDE";
	private static final String DE = "DE";
	
	private static final String DVERFOR = "dVerFor";
	private static final String VERSION = "150";
	
	private static final String RUC_RECEPTOR = "dRucRec";
	private static final String NUMID_RECEPTOR = "dNumIDRec";
	
	private static final String NVERSION = "nVersion";
	private static final String DRUCREC = "dRucRec";
	private static final String DNUMIDREC = "dNumIDRec";
	private static final String DTOTGRALOPE = "dTotGralOpe";
	private static final String DTOTIVA = "dTotIVA";
	private static final String CITEMS = "cItems";
	private static final String DIGESTVALUE = "DigestValue";
	private static final String IDCSC = "IdCSC";
	private static final String CHASHQR = "cHashQR";
	private static final String NORMAL = "Normal";
	private static final String PROCESO_ID = "procesoId";
	private static final String DOCUMENTO_ID = "documentoId";
	

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentoElectronicoServiceImpl.class.getName());

    private static final String CDC_INVALIDO = "CDC inválido";
    private static final String CDC_NO_ENCONTRADO = "CDC no existente en el SIFEN, consulte con el emisor del documento";
    private static final String ERROR_LECTURA_XML ="Excepción durante la lectura del XML.";
   
    private static final BigDecimal MARGEN_ERROR_BD = new BigDecimal("1");
	
	private static final String JSON_DATE_TIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
	
	/**
     * lista de parámetros recibidos con el QR para la versión 141 0 nVersion
     * (versión de generación del QR) 1 Id (CDC del correspondiente DE) 2 dFeEmiDE
     * (Fecha y Hora de emisión del DE) 3/4 dRucRec/dNumIDRec (Identificación del
     * receptor: RUC o CI) 5 dTotGralOpe (total general de la operación) 6 dTotIVA
     * (liquidación total del IVA) 7 cItems (cantidad de ítems del DE) 8 DigestValue
     * (hash de la firma digital del DE) 9 IdCSC (identificador del código entregado
     * por el sifen) 10 cHashQR (código Hash de los parámetros)
     * 
     * {"nVersion", "Id", "dFeEmiDE", "dRucRec", "dNumIDRec",
    		DTOTGRALOPE_TAG, "dTotIVA", "cItems", DIGEST, "IdCSC", "cHashQR"}
     */
    private static final String[] PARAM_QR_V_141 = {NVERSION, "Id", FECHA_EMISION, FECHA_EMISION, NUMID_RECEPTOR,
    		DTOTGRALOPE_TAG, DTOTIVA, CITEMS, DIGEST, IDCSC, CHASHQR};
	

	private static final List<BigInteger> TIPO_DOCUMENTO_AVAILABLE_LIST = new ArrayList<>(Arrays.asList(
			BigInteger.valueOf(1),
			BigInteger.valueOf(4),
			BigInteger.valueOf(5),
			BigInteger.valueOf(6),
			BigInteger.valueOf(7),
			BigInteger.valueOf(9),
			BigInteger.valueOf(10)));
	
	GsonBuilder builder = new GsonBuilder();
	Gson gson = builder.setDateFormat(JSON_DATE_TIME_FORMAT).create();
		
	/**
	 * Recepción de documento electrónico
	 * @throws SIFENException 
	 */
	public Map<String, Object> generarDocumentoElectronico(String documento, Long usuario) 
			throws SIFENException {
		
		Document xmlDocument = transformRdeToXml(documento, usuario);
		String cdcGenerado = addCdcVersionToXml(xmlDocument);
		Map<String, Object> mapResponse = new HashMap<>();
		String xmlSigned = procesarDocumentoParaFirma(xmlDocument);
		mapResponse.put("CDC", cdcGenerado);
		mapResponse.put("factura", xmlSigned);
		return mapResponse;
	}

	/**
	 * Validamos datos del xml
	 * @param documentElectronico
	 * @throws SIFENException
	 */
	private void verificarDatosDelXml(TDE documentElectronico) 
			throws SIFENException {
				
		if(!TIPO_DOCUMENTO_AVAILABLE_LIST.contains(documentElectronico.getGTimb().getITiDE())) {
			throw new SIFENException("Tipo de Documento Electrónico no habilitado para E-Kuatia'i");
		}		
		
		validarReceptor(documentElectronico);
		validarGCamCond(documentElectronico);
		
		final List<BigInteger> gCamItemLis = new ArrayList<>(Arrays.asList(
				BigInteger.valueOf(1),
				BigInteger.valueOf(4),
				BigInteger.valueOf(5),
				BigInteger.valueOf(6)));
		
		if(documentElectronico.getGTimb().getITiDE() != null 
			&& gCamItemLis.contains(documentElectronico.getGTimb().getITiDE())) {			
			validateGCamItem(documentElectronico);		
		}
	}

	/**
	 * Validar Datos del receptor
	 * @param documentElectronico
	 * @throws SIFENException
	 */
    private void validarReceptor(TDE documentElectronico) throws SIFENException {
    	
		if(documentElectronico.getGDatGralOpe().getGDatRec().getDNomRec() == null 
			||  documentElectronico.getGDatGralOpe().getGDatRec().getDNomRec().isEmpty()) {
			throw new SIFENException("Nombre del Receptor es Requerido");
		}
		
		if(documentElectronico.getGDatGralOpe().getGDatRec().getINatRec() == null) {
				throw new SIFENException("Naturaleza del Receptor es Requerido");
		}
		
		if(documentElectronico.getGDatGralOpe().getGDatRec().getITiOpe() == null) {
				throw new SIFENException("Tipo de Operación del Receptor es Requerido");
		}
		
		//Obligatorio si iNatRec = 1
		if(documentElectronico.getGDatGralOpe().getGDatRec().getINatRec().compareTo(BigInteger.valueOf(1)) == 0 &&
				(documentElectronico.getGDatGralOpe().getGDatRec().getDRucRec() == null ||
				 documentElectronico.getGDatGralOpe().getGDatRec().getDRucRec().isEmpty() ||
				 documentElectronico.getGDatGralOpe().getGDatRec().getDDVRec() == null)) {
				throw new SIFENException("RUC y DV del Receptor es Requerido");
		}
		
		//Obligatorio si iNatRec = 2 y iTiOpe ≠ 4
		if(documentElectronico.getGDatGralOpe().getGDatRec().getINatRec().compareTo(BigInteger.valueOf(2)) == 0 &&
				documentElectronico.getGDatGralOpe().getGDatRec().getITiOpe().compareTo(BigInteger.valueOf(4)) != 0 &&
				(documentElectronico.getGDatGralOpe().getGDatRec().getDNumIDRec() == null ||
				 documentElectronico.getGDatGralOpe().getGDatRec().getDNumIDRec().isEmpty() ||
				 documentElectronico.getGDatGralOpe().getGDatRec().getITipIDRec() == null)) {
				throw new SIFENException("Nro. y tipo de documento de identidad del Receptor es Requerido");
		}
		
	}
    
    
    /**
	 * Validar condición de la operación
	 * @param documentElectronico
	 * @throws SIFENException
	 */
    private void validarGCamCond(TDE documentElectronico) throws SIFENException {

    	final List<BigInteger> iCondOpeList = new ArrayList<>(Arrays.asList(
    			BigInteger.valueOf(1),
    			BigInteger.valueOf(2)));
    	
    	//Obligatorio si iTiDE = 1 o 4 o 9 o 10
    	//No informar si iTiDE ≠ 1 o 4 o 9 o 10
		if(documentElectronico.getGTimb().getITiDE().compareTo(BigInteger.valueOf(1)) == 0 ||
				documentElectronico.getGTimb().getITiDE().compareTo(BigInteger.valueOf(4)) == 0  ||
				documentElectronico.getGTimb().getITiDE().compareTo(BigInteger.valueOf(9)) == 0||
				documentElectronico.getGTimb().getITiDE().compareTo(BigInteger.valueOf(10)) == 0) {
			
			if(documentElectronico.getGDtipDE().getGCamCond() == null) {
				throw new SIFENException("Campos de la Condición de la Operación es Requerido");
			}
			
			if(documentElectronico.getGDtipDE().getGCamCond().getICondOpe() == null ||
					! iCondOpeList.contains(documentElectronico.getGDtipDE().getGCamCond().getICondOpe())) {
					throw new SIFENException("Condición de la Operación es Requerido");
			}
			
		} else if(documentElectronico.getGDtipDE().getGCamCond() != null) {
			throw new SIFENException("Campos de la Condición de la Operación NO se debe Informar");
		}
	}

    /**
     * Validar el grupo de detalle de la Operacion gCamItem
     * @param TDE documentElectronico
     * @throws SIFENException
     */
	private void validateGCamItem(TDE documentElectronico ) throws SIFENException {

		final BigDecimal toleranciaDecimal = BigDecimal.valueOf(1);
		
		BigDecimal sumaTotOpeGs = BigDecimal.ZERO;
		BigDecimal sumaTotOpeItem = BigDecimal.ZERO;
		
		BigDecimal sumaTotIVA = BigDecimal.ZERO;
		BigDecimal sumaBasGravIVA = BigDecimal.ZERO;
		
		List<TgCamItem> gCamItem = documentElectronico.getGDtipDE().getGCamItem();
		
		if (gCamItem == null || gCamItem.isEmpty()) {
			throw new SIFENException("Detalles de la operación Incorrecto");
		}
		
		BigDecimal calcDTotOpeItem;
		BigDecimal calcDescuentoItem;

		BigDecimal calcDBasGravIVA;
		BigDecimal calcDLiqIVAItem;
		BigDecimal calcTasaIVA;
		
		for (TgCamItem tgCamItem : gCamItem) {
			
			if(tgCamItem.getGValorItem() != null 
				&& tgCamItem.getGValorItem().getGValorRestaItem() != null) {

				// (dPUniProSer – dDescItem – dDescGloItem – dAntPreUniIt – dAntGloPreUniIt) * dCantProSer					
				calcDescuentoItem = valueBigDecimal(tgCamItem.getGValorItem().getGValorRestaItem().getDDescItem()).add(
						valueBigDecimal(tgCamItem.getGValorItem().getGValorRestaItem().getDDescGloItem())).add(
								valueBigDecimal(tgCamItem.getGValorItem().getGValorRestaItem().getDAntPreUniIt())).add(
										valueBigDecimal(tgCamItem.getGValorItem().getGValorRestaItem().getDAntGloPreUniIt()));
				//	Cálculo para Autofactura (C002=4): dPUniProSer * dCantProSer
				
				calcDTotOpeItem = tgCamItem.getDCantProSer().multiply(
						tgCamItem.getGValorItem().getDPUniProSer().subtract(calcDescuentoItem));
				
				if (calcDTotOpeItem.subtract(
								tgCamItem.getGValorItem().
							getGValorRestaItem().getDTotOpeItem()).compareTo(
						MARGEN_ERROR_BD) > 0) {
					throw new SIFENException("Cálculo de Total Item incorrecto del item: " + tgCamItem.getDDesProSer());
				}
				
				//gCamIVA No informar si iTImp=2 y iTiDE= 4 o 7
				if(documentElectronico.getGTimb().getITiDE().compareTo(BigInteger.valueOf(4)) != 0 &&
						documentElectronico.getGDatGralOpe().getGOpeCom().getITImp().compareTo(BigInteger.valueOf(2)) != 0)  {
				
					// [dTotOpeItem * (dPropIVA/100)] / (dTasaIVA/100)+1				
					calcTasaIVA = 
							new BigDecimal(tgCamItem.getGCamIVA().getDTasaIVA()).divide(new BigDecimal(100));
					calcDBasGravIVA = calcDTotOpeItem.multiply(tgCamItem.getGCamIVA().getDPropIVA().divide(
							new BigDecimal(100)));
					calcDBasGravIVA = calcDBasGravIVA.divide(calcTasaIVA.add(BigDecimal.ONE),8, RoundingMode.HALF_DOWN);
					
					
					//Si iAfecIVA = 2 o 3 este campo es	igual 0
					if(tgCamItem.getGCamIVA().getIAfecIVA() == null) {
						tgCamItem.getGCamIVA().setIAfecIVA(BigInteger.ONE);
						if(tgCamItem.getGCamIVA().getDTasaIVA() == null ||
								tgCamItem.getGCamIVA().getDTasaIVA().compareTo(BigInteger.ZERO) ==0) {
							tgCamItem.getGCamIVA().setIAfecIVA(BigInteger.valueOf(3));
						}
					}
					
					calcDBasGravIVA = tgCamItem.getGCamIVA().getIAfecIVA().intValue() != 1 && tgCamItem.getGCamIVA().getIAfecIVA().intValue() != 4 ?
							BigDecimal.ZERO : calcDBasGravIVA;
					
					if (calcDBasGravIVA.subtract(tgCamItem.getGCamIVA().getDBasGravIVA()).abs()
							.compareTo(toleranciaDecimal) > 0) {
						throw new SIFENException("Cálculo de Base Gravada incorrecto del item: " + tgCamItem.getDDesProSer());
					}
					sumaBasGravIVA = sumaBasGravIVA.add(calcDBasGravIVA);
					
					// dBasGravIVA * (dTasaIVA/100)
					calcDLiqIVAItem = calcDBasGravIVA.multiply(calcTasaIVA).setScale(8, BigDecimal.ROUND_HALF_DOWN);
					//Si iAfecIVA = 2 o 3 este campo es	igual 0
					calcDLiqIVAItem = tgCamItem.getGCamIVA().getIAfecIVA().intValue() != 1 && tgCamItem.getGCamIVA().getIAfecIVA().intValue() != 4 ?
							BigDecimal.ZERO : calcDLiqIVAItem;
					
					if (calcDLiqIVAItem.subtract(tgCamItem.getGCamIVA().getDLiqIVAItem()).abs()
							.compareTo(toleranciaDecimal) > 0) {
						throw new SIFENException("Cálculo de Liquidación de IVA incorrecto del item: " + tgCamItem.getDDesProSer());
					}
					sumaTotIVA = sumaTotIVA.add(calcDLiqIVAItem);
				}
				
				if(tgCamItem.getGValorItem().getGValorRestaItem().getDTotOpeGs() != null) {
					sumaTotOpeGs = sumaTotOpeGs.add(tgCamItem.getGValorItem().
							getGValorRestaItem().getDTotOpeGs());
				}
				
				if(tgCamItem.getGValorItem().getGValorRestaItem().getDTotOpeItem() != null) {
					sumaTotOpeItem = sumaTotOpeItem.add(tgCamItem.getGValorItem().
							getGValorRestaItem().getDTotOpeItem());
				}
			}			
		}
	
		BigDecimal dTotalGs = valueBigDecimal(documentElectronico.getGTotSub().getDTotalGs());
		BigDecimal dTotGralOpe = documentElectronico.getGTotSub().getDTotGralOpe();
		
		BigDecimal dTotIVA10And5 = valueBigDecimal(documentElectronico.getGTotSub().getDIVA10()).add(
				valueBigDecimal(documentElectronico.getGTotSub().getDIVA5()));
		
		BigDecimal dTotBaseGrav10And5 = valueBigDecimal(documentElectronico.getGTotSub().getDBaseGrav10()).add(
				valueBigDecimal(documentElectronico.getGTotSub().getDBaseGrav5()));
		
		
		if(dTotalGs.compareTo(BigDecimal.ZERO)==0 &&
				dTotGralOpe.compareTo(BigDecimal.ZERO)==0) {
			throw new SIFENException("Cálculo de Total de la Operación por Item incorrecto");
		}
		
		
		//Cuando C002=4 corresponde a F014 (dTotGralOpe)		
    	if(!documentElectronico.getGDatGralOpe().getGOpeCom().getCMoneOpe().equals("PYG")) {
    		
    		//Si cMoneOpe ≠ PYG y dCondTiCam = 1, corresponde al cálculo aritmético: dTotGralOpe * dTiCam
    		if(documentElectronico.getGDatGralOpe().getGOpeCom().getDCondTiCam()==1 &&
    			dTotalGs.subtract(
    					dTotGralOpe.multiply(valueBigDecimal(documentElectronico.getGDatGralOpe().getGOpeCom().getDTiCam()))
    					).compareTo(MARGEN_ERROR_BD) > 0) {
    			throw new SIFENException("Cálculo de Total de la Operación Gs. incorrecto");
    		}

    		//Si cMoneOpe ≠ PYG y dCondTiCam = 2, corresponde a la suma de todas las ocurrencias de dTotOpeGs
    		if(documentElectronico.getGDatGralOpe().getGOpeCom().getDCondTiCam()==2 &&
        			dTotalGs.compareTo(sumaTotOpeGs) != 0) {
        			throw new SIFENException("Cálculo de Total de la Operación Gs. incorrecto");
        	}
		}
    	else if(BigDecimal.ZERO.compareTo(valueBigDecimal(dTotalGs)) != 0) {
			throw new SIFENException("Total de la Operación Gs. incorrecto");    		
    	}
    	
    	if(dTotGralOpe.subtract(sumaTotOpeItem).compareTo(MARGEN_ERROR_BD) > 0) {
			throw new SIFENException("Cálculo de Total de la Operación incorrecto");
		}
    	
    	// No debe existir el campo si iTImp≠ 1 o iTImp≠5
    	if(documentElectronico.getGDatGralOpe().getGOpeCom().getITImp().compareTo(BigInteger.valueOf(1)) == 0 ||
    			documentElectronico.getGDatGralOpe().getGOpeCom().getITImp().compareTo(BigInteger.valueOf(5)) == 0) {
    		
	   	 	if (dTotIVA10And5.subtract(valueBigDecimal(documentElectronico.getGTotSub().getDTotIVA())).abs()
					.compareTo(toleranciaDecimal) > 0) {
				throw new SIFENException("Cálculo Total de IVA incorrecto");
			}
	   	 		   	 	
			if (dTotBaseGrav10And5.subtract(valueBigDecimal(documentElectronico.getGTotSub().getDTBasGraIVA())).abs()
					.compareTo(toleranciaDecimal) > 0) {
				throw new SIFENException("Cálculo Total de Base Gravada incorrecto");
			}
    	}
	}

	private void validateParam(Map<String, Object> parametros, String procesoId, String documentoId) 
    		throws SIFENException {
    	if (parametros != null) {
    		if (!parametros.containsKey(procesoId) || !parametros.containsKey(documentoId)) {
                throw new SIFENException("Faltan parametros o parametros Incorrectos");
            }
    	}else {
    		throw new SIFENException("Parametros no recibidos.");
    	}		
	}
	
	private BigDecimal valueBigDecimal (BigDecimal value) {
		return  (value == null ? BigDecimal.ZERO : value);
	}

	@Override
    public byte[] getKuDE(String cdc) throws SIFENException {

		String reportName;
		
		switch (cdc.substring(0, 2)) {
			case "01":
				reportName = JasperUtils.REPORT_KUDE_FE;
	            break;
			case "04":
				reportName = JasperUtils.REPORT_KUDE_AFE;
	            break;
			case "05":
				reportName = JasperUtils.REPORT_KUDE_NC_ND;
	            break;
			case "06":
				reportName = JasperUtils.REPORT_KUDE_NC_ND;
	            break;
			case "07":
				reportName = JasperUtils.REPORT_KUDE_NRE;
	            break;
	 		case "09":
	 			reportName = JasperUtils.REPORT_KUDE_BVE;
	            break; 
	 		case "10":
	 			reportName = JasperUtils.REPORT_KUDE_BRS;
	            break; 
	 		default: 
	 			reportName = JasperUtils.REPORT_KUDE;
                break;
        }
		    	    	 
    	if (!isDigitoVerificadorValido(cdc.substring(0, cdc.length() - 1), 11, cdc.substring(cdc.length() - 1))) {
            throw new SIFENException(CDC_INVALIDO);
        }
return null;
        /*List<DocumentoElectronicoXml> listDocumentXml = documentoElectronicoXmlService.getById(cdc);
        if (listDocumentXml.isEmpty()) {
        	throw new SIFENException(CDC_NO_ENCONTRADO);
        }
             
        try (Connection connection = dataSource.getConnection()) {
        	
        	DocumentoElectronicoXml documentoElectronicoXml = listDocumentXml.get(0);
			return JasperUtils.generarKuDE(reportName, 
					documentoElectronicoXml.getXml(), connection, isCanceledDTE(cdc));

		} catch (JRException | IOException e) {
        	LOGGER.error("JRException", e);
       	 	throw new SIFENException("Error al generar el KuDE");
		} catch (Exception e) {
        	LOGGER.error("Exception", e);
       	 	throw new SIFENException("Error gral. al generar el KuDE");
		}*/
       
    }
    
    
    private boolean isDigitoVerificadorValido(String valor, Integer baseMax, String digitoDeComparacion) {
    	return Utils.calcularYComparar(valor, baseMax, digitoDeComparacion) == 0;
    }
	
	private String getIdentificacionReceptor(Document xmlDocument) {

		String idReceptor = null;
		try {
			idReceptor = obtenerValorElemento(xmlDocument, RUC_RECEPTOR);
		} catch (Exception e) {
			//Do nothing
		}
		
		if(idReceptor == null || idReceptor.isEmpty()) {
			try {
				idReceptor = obtenerValorElemento(xmlDocument, NUMID_RECEPTOR);
			} catch (Exception e) {
				//Do nothing
			}
		}
		return idReceptor;
	}
	
	private TDE extractRde(String documentMap) {
		return gson.fromJson(documentMap, TDE.class);
	}

	/**
	 * Preparamos el documento a enviar
	 * @param xmlDocument
	 * @return Document
	 * @throws TransformerException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private Document documentToSend(Document xmlDocument) throws TransformerException, 
			IOException, SAXException, ParserConfigurationException {
		DOMSource domSource = new DOMSource(xmlDocument);
		Element element = (Element) XmlUtils.getNodeFromPath(domSource.getNode(), RDE);		
		String elementString = XmlUtils.documentToString(element);	
		
		return  XmlUtils.stringToDocument(elementString);
	}

	/**
	 * Extraemos RDE a un Document
	 * @param documento
	 * @param usuario 
	 * @return Document
	 * @throws SIFENException 
	 */
	private Document transformRdeToXml(String documento, Long usuario) 
			throws SIFENException{
		try {
			TDE de = extractRde(documento);
			de.setDDVId(BigInteger.valueOf(1));//digito verificador
			de.setDSisFact(1);
			de.setDFecFirma(new Date(System.currentTimeMillis()));
			
			Date feEmiDE = de.getGDatGralOpe().getDFeEmiDE();

			Calendar calendar = Calendar.getInstance();
			Calendar calfeEmiDE = Calendar.getInstance();
			
			calfeEmiDE.setTimeInMillis(feEmiDE.getTime());			
			calfeEmiDE.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			calfeEmiDE.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			calfeEmiDE.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
			
			feEmiDE.setTime(calfeEmiDE.getTimeInMillis());
			de.getGDatGralOpe().setDFeEmiDE(feEmiDE);
			
			obtenerOperacionDEAndDatosTimbradoAndEmisor(de, usuario);
			RDE rde = new RDE();
			rde.setDVerFor(BigInteger.valueOf(Long.valueOf(VERSION)));
			rde.setDE(de);	
			verificarDatosDelXml(de);
			return this.converterObjectForXml(rde);
			
		} catch (Exception e) {
			throw new AttributeNotFoundException("Excepción durante la lectura del documento. " + e.getMessage());
		}
		
	}
	
	private Document converterObjectForXml(Object clase) 
			throws IOException, SAXException, ParserConfigurationException {
		StringWriter sw = new StringWriter();

			JAXB.marshal(clase, sw);
			String xmlString = sw.toString();				
			xmlString = xmlString.substring(xmlString.indexOf('\n')+1);
			xmlString = xmlString.replace("RDE", RDE);
			xmlString = xmlString.replace("ns2:", "");
			xmlString = xmlString.replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
			xmlString = xmlString.replace("ns3:", "");
			xmlString = xmlString.replace(":ns3", "");

			return XmlUtils.stringToDocument(xmlString);

	}

	/**
	 * Obtenemos parametros que nos falta en el DE
	 * @param usuario 
	 * @param rde
	 * @throws ParseException 
	 * @throws SIFENException 
	 */
	private void obtenerOperacionDEAndDatosTimbradoAndEmisor(TDE de, Long usuario) 
			throws ParseException, SIFENException {
		
		/*UsuariosFacturadores usuarioFact = obtenerDatosUsuarioFacturador(usuario);
		ContribuyenteDTO dto = obtenerDatosContribuyente(usuarioFact.getRuc());
		validarFechaEmision(de);
		// dNumDoc al grabar realizar el cambio en la 
		//tabla ek_tipos_documentos_puntos campo ultimo_numero
		TgCOpeDE gOpeDE = new TgCOpeDE();
		TgEmis gEmis = new TgEmis(); 
		Long codSeg = (long) (Math.random()* 99999999 + 100000000);
		
		gOpeDE.setITipEmi(BigInteger.valueOf(1));//1= Normal
		gOpeDE.setDDesTipEmi(NORMAL);
		gOpeDE.setDCodSeg(BigInteger.valueOf(codSeg));
		de.setGOpeDE(gOpeDE);
		
		if(de.getGTimb() != null && de.getGTimb().getITiDE() != null) {
			List<TiposDocumentosPunto> tiposDocumentos = obtenemosTiposDocumentos(usuarioFact);
			
			for (TiposDocumentosPunto tiposDocumentosPunto : tiposDocumentos) {
				if(tiposDocumentosPunto.getEkTiposDocumento().getCodigoSifen().
						toBigInteger().equals(de.getGTimb().getITiDE())){
					
					de.getGTimb().setDDesTiDE(this.parametroDetalleTipoDocumentoElectronico(tiposDocumentosPunto.
							getEkTiposDocumento().getCodigoSifen().toString())); //dDesTiDE
					de.getGTimb().setDNumDoc(formatoParametros(
							tiposDocumentosPunto.getUltimoNumero().
							add(new BigDecimal(1)).toString(), 7));//dNumDoc
					de.getGTimb().setDSerieNum(verificarDatosCampo(tiposDocumentosPunto.getSerie()));
				}
			}
			
			de.getGTimb().setDNumTim(formatoParametros(
					usuarioFact.getEkTimbrados().get(0).getTimbrado().toString(), 8));//dNumTim
			de.getGTimb().setDEst(formatoParametros(
					usuarioFact.getEkTimbrados().get(0).getEkEstablecimientos().get(0).
					getEstablecimiento().toString(), 3));//dEst
			de.getGTimb().setDPunExp(formatoParametros(
					usuarioFact.getEkTimbrados().get(0).getEkEstablecimientos().get(0).
					getEkPuntos().get(0).getPunto().toString(), 3));//dPunExp	
			de.getGTimb().setDFeIniT(new SimpleDateFormat("yyyy-MM-dd").parse(
					usuarioFact.getEkTimbrados().get(0).
					getFechaInicioVigencia().toString()));//dFeIniT
		}else {
			throw new SIFENException(ERROR_MESSAGE + "gTimb");
		}
			
		gEmis.setDRucEm(usuarioFact.getRuc());
		gEmis.setDDVEmi(usuarioFact.getDv().toBigInteger());
		gEmis.setITipCont(new BigInteger(AppEkConstants.TipoContribuyenteEnum.
				getCodigo(usuarioFact.getTipoContribuyente())));
		gEmis.setDNomEmi(usuarioFact.getNombreRazonSocial());
		gEmis.setDDirEmi(usuarioFact.getEkTimbrados().get(0).
				getEkEstablecimientos().get(0).getNombreUbicacion());
		gEmis.setCDepEmi(usuarioFact.getEkTimbrados().get(0).
				getEkEstablecimientos().get(0).getDepartamento().toBigInteger());
		
		gEmis.setDNumCas(BigInteger.valueOf(0));
		gEmis.setDDesDepEmi(dto.getDepartamento());
		gEmis.setCCiuEmi(new Integer(usuarioFact.getEkTimbrados().get(0).
				getEkEstablecimientos().get(0).getDistrito().toString()));
		gEmis.setDDesCiuEmi(dto.getDistrito());
		gEmis.setDTelEmi(verificarDatosCampo(dto.getTelefono()));
		gEmis.setDEmailE(verificarDatosCampo(dto.getCorreoElectronico()));
		
		List<TgActEco> listTgActEco = obtenerActividadEconomica(usuarioFact.
	    		getEkConfiguracionesUsuarios().get(0).getId(), usuarioFact.getRuc());
		gEmis.getGActEco().addAll(listTgActEco);
		
		de.getGDatGralOpe().setGEmis(gEmis);*/
	}

	/**
	 * Validamos con la fecha del sistema y con la fecha de inicio y fin de vigencia
	 * @param documento
	 * @param usuarioFact
	 * @throws SIFENException
	 */
	private void validarFechaEmision(TDE documento) 
			throws SIFENException{
		Date fechaActual = new Date();
		
		int resultado = compararFechas(documento.getGDatGralOpe().getDFeEmiDE(), fechaActual);
		
		if(resultado > 0 ) {
			throw new SIFENException("La fecha de emisión del comprobante no puede ser mayor a la "
					+ "fecha actual del sistema");
		}		
	}

	private int compararFechas(Date dFeEmiDE, Date fechaComparar) {
		return dFeEmiDE.compareTo(fechaComparar);
	}

	private String verificarDatosCampo(String valor) {
		if(valor != null && !valor.isEmpty()) {
			return valor;
		}else {
			return "";
		}
	}

	/**
	 * Lista de Actividad Económica
	 * @param idUsuario
	 * @param ruc
	 * @return List<TgActEco>
	 */
	private List<TgActEco> obtenerActividadEconomica(Long idUsuario, String ruc) {
		List<TgActEco> activEcoUsuario = new ArrayList<>();
		/*List<ConfiguracionesActividade> configActiv = configuracionesActividadesService.getByConfiguracionId(idUsuario);
		List<ActividadEconomicaDTO> configActivDescripcion = configuracionesUsuarioService.getActividadesEconomicas(ruc);
		
		for (ConfiguracionesActividade configuracionesActividade : configActiv) {
			for (ActividadEconomicaDTO actividadEconomicaDescrip : configActivDescripcion) {
				
				if(actividadEconomicaDescrip.getSigla().equals(configuracionesActividade.getId().getSigla())) {
					TgActEco gActEco = new TgActEco();
					gActEco.setCActEco(configuracionesActividade.getId().getSigla());
					gActEco.setDDesActEco(actividadEconomicaDescrip.getNombre());
					activEcoUsuario.add(gActEco);
					break;
				}
				
			}
			
		}*/
		return activEcoUsuario;
	}
/*
	private List<TiposDocumentosPunto> obtenemosTiposDocumentos(UsuariosFacturadores usuarioFact) 
			throws SIFENException {
		try {
			return usuarioFact.getEkTimbrados().get(0).
						getEkEstablecimientos().get(0).getEkPuntos().get(0).getEkTiposDocumentosPuntos();
		}catch(Exception e) {
			throw new SIFENException(ERROR_MESSAGE + " EkPuntos");
		}
	}
	
	private UsuariosFacturadores obtenerDatosUsuarioFacturador(Long usuario) 
			throws SIFENException {
		if(usuario == null) {
			throw new SIFENException(ERROR_MESSAGE + "usuario");
		}
		try {
			return usuariosFacturadoresService.obtenerUsuarioFacturador(usuario);
		} catch (Exception e) {
			throw new SIFENException(ERROR_MESSAGE + "Datos del Usuario Facturador. " 
			+ e.getMessage());
		}
	}*/

	private ContribuyenteDTO obtenerDatosContribuyente(String ruc) 
			throws SIFENException {
		if(ruc == null || ruc.isEmpty()) {
			throw new SIFENException(ERROR_MESSAGE + "RUC");
		}
		
		//TODO: En properties
		
		try {
			return null;
		} catch (Exception e) {
			throw new SIFENException(ERROR_MESSAGE + "Datos del Contribuyente. " 
			+ e.getMessage());
		}
	}

	private String formatoParametros(String valor, Integer longitud) 
			throws SIFENException{
		try (Formatter fmt = new Formatter()){
			if (valor.length() < longitud) {	
				String formatear = "%0"+longitud+"d";
				fmt.format(formatear, new Integer(valor));
				valor = fmt.toString();
			}
		}catch (Exception e) {
			throw new SIFENException("Error al Formatear parametros");
		}
		return valor;
	}

	/**
	 * Generar y añadir QR
	 * @param xmlDocument
	 * @param idCsc
	 * @throws SIFENException
	 */
	private void generateAndAddQRtoXml(Document xmlDocument, String idCsc) throws SIFENException {
		Map<String, Object> paramGeneral = new HashMap<>();
		
		try {
			paramGeneral.put(NVERSION, getParametrosCdc(xmlDocument, DVERFOR));
			paramGeneral.put(ID, getId(xmlDocument));
			paramGeneral.put(FECHA_EMISION, toHex(getParametrosCdc(xmlDocument, FECHA_EMISION)));
			paramGeneral.put(DRUCREC, getParametrosQR(xmlDocument, DRUCREC));
			paramGeneral.put(DNUMIDREC, getParametrosQR(xmlDocument, DNUMIDREC));
			paramGeneral.put(DTOTGRALOPE, getParametrosQR(xmlDocument, DTOTGRALOPE));
			paramGeneral.put(DTOTIVA, getParametrosQR(xmlDocument, DTOTIVA));
			paramGeneral.put(CITEMS, contarItems(xmlDocument, "gCamItem"));
			paramGeneral.put(DIGESTVALUE, toHex(getParametrosCdc(xmlDocument, DIGESTVALUE)));
			paramGeneral.put(IDCSC, getIdCSC(idCsc));
			String msg = getMsg(paramGeneral);
			String cSC = getCSC(idCsc,xmlDocument);
			
			String hashHex = DigestUtils.sha256Hex(msg + cSC);
			String qR2 = sifenUrlConsultaQr + msg + "&" + CHASHQR + "=" + hashHex;
			
			//definimos el nodo raiz
			Element eRaiz = (Element) xmlDocument.getElementsByTagName(RDE).item(0);
			// definimos el nodo que contendrá la versión
			Element gCamFuFD = xmlDocument.createElement("gCamFuFD");
			eRaiz.appendChild(gCamFuFD);
			// definimos cada uno de los elementos y le asignamos un valor
			Element dCarQR = xmlDocument.createElement(QR);
			dCarQR.appendChild(xmlDocument.createTextNode(qR2));
			gCamFuFD.appendChild(dCarQR);
			
		} catch (Exception e) {
			
			LOGGER.error("Error al generar el código QR: {}",e.getMessage());
		}
	}

	private String getCSC(String id, Document xmlDocument) throws SIFENException {
		
		String dRucEm = getParametrosCdc(xmlDocument, RUC_EMISOR);
		//TODO: En properties
		String codigoSeguridad = "obtener";
		if(codigoSeguridad == null) {
			throw new SIFENException(ERROR_MESSAGE + "CSC");
		}
		return codigoSeguridad;
	}

	/**
	 * Concatenamos los parametros para la url
	 * @param paramGeneral
	 * @return msg
	 */
	private String getMsg(Map<String, Object> paramGeneral) {
		String msg = NVERSION + "=" 		+ paramGeneral.get(NVERSION)				
				+ "&" + ID + "="			+ paramGeneral.get(ID)
				+ "&" + FECHA_EMISION + "=" 		+ paramGeneral.get(FECHA_EMISION);
				if(paramGeneral.get(DNUMIDREC) == null || paramGeneral.get(DNUMIDREC).equals("0"))
					msg += "&" + DRUCREC + "=" + paramGeneral.get(DRUCREC);
				else
					msg += "&" + DNUMIDREC + "=" + paramGeneral.get(DNUMIDREC);
				
				msg += "&" + DTOTGRALOPE + "=" 	+ paramGeneral.get(DTOTGRALOPE)
				+ "&" + DTOTIVA + "="			+ paramGeneral.get(DTOTIVA)
				+ "&" + CITEMS + "="			+ paramGeneral.get(CITEMS)
				+ "&" + DIGESTVALUE + "=" 		+ paramGeneral.get(DIGESTVALUE)
				+ "&" + IDCSC + "=" 			+ paramGeneral.get(IDCSC);
		return msg;
	}

	/**
	 * Completamos 0 a la izquierda para idCsc
	 * @param idCsc
	 * @return String
	 * @throws SIFENException
	 */
	private String getIdCSC(String idCsc) throws SIFENException {

		try (Formatter fmt = new Formatter()){		
			fmt.format("%04d", new Integer(idCsc));
			idCsc = fmt.toString();
		}catch (Exception e) {
			throw new SIFENException(ERROR_MESSAGE + "idCsc");
		}
		return idCsc;
	}

	/**
	 * Contamos gCamItem en el xml
	 * @param xmlDocument
	 * @param tag
	 * @return cantidad
	 */
	private String contarItems(Document xmlDocument, String tag) {
		String result = null;
		NodeList nodoList = xmlDocument.getElementsByTagName(tag);
		if(nodoList.getLength() > 0) {
			result = Integer.toString(nodoList.getLength());
		}else {
			result = "0";
		}
		return result;
	}

	/**
	 * Obtenemos parametros para armar el QR
	 * @param xmlDocument
	 * @param tag
	 * @return
	 */
	private String getParametrosQR(Document xmlDocument, String tag) {
		String result = null;
		Element element = getElement(xmlDocument, tag);
		if(element == null) {
			result = "0";
		}else {
			result = element.getTextContent();
		}
		
		return result;
	}

	/**
	 * Obtenemos el Id del elemento DE
	 * @param xmlDocument
	 * @return iD
	 */
	private String getId(Document xmlDocument) {
		Element eDE = getElement(xmlDocument, DE);
		return eDE.getAttribute(ID);
	}

	/**
	 * Firmamos el documento
	 * @param xmlDocument
	 * @return Document
	 * @throws SIFENException
	 */
	private String procesarDocumentoParaFirma(Document xmlDocument) throws SIFENException {
		try {
			return utilsService.firmar(xmlDocument);
			
		} catch (Exception e) {
			throw new SIFENException("Error al procesar Documento. " + e.getMessage());
		}
	}

	/**
	 * Añadimos version y cdc al xml
	 * @param xmlDocument
	 * @throws SIFENException
	 */
	private String addCdcVersionToXml(Document xmlDocument) throws SIFENException {
		
		try {	
			String cdc = calculateCDC(xmlDocument);
			String dv = Utils.calcularDVExclusivoTest(cdc);
			String cdcAndDv =cdc + dv;
			
			//Añadirmos el tag correspondiente en el xml procesamos el nodo
			Element eDE = getElement(xmlDocument, DE);
			if (eDE == null) {
				throw new XMLReadException("No se encontro tag \"DE\"");
			}
			// agregar a Id como atributo de DE
			Attr attr = xmlDocument.createAttribute(ID);
			attr.setValue(cdcAndDv);
			eDE.setAttributeNode(attr);
			
			Element dDVId = getElement(xmlDocument, "dDVId");
			if (dDVId == null) {
				throw new XMLReadException("No se encontro tag \"dDVId\"");
			}
			dDVId.setTextContent(dv);
			
			//definimos el nodo raiz
			Element raiz = (Element) xmlDocument.getElementsByTagName(RDE).item(0);
			raiz.appendChild(eDE);
			
			xmlDocument.normalize();
			
			return cdcAndDv;
			
		}catch (Exception e) {
			throw new SIFENException("Error. " + e.getMessage());
		}
	}

	private String calculateCDC(Document xmlDocument) throws SIFENException {
		String iTiDE = getTipoDocumento(xmlDocument);
		String dRucEm = getRucEmisor(xmlDocument);
		String dDVEmi = verificarLongitud(getParametrosCdc(xmlDocument, DIGITO_VERIFICADOR), 1);
		String dEst = verificarLongitud(getParametrosCdc(xmlDocument, ESTABLECIMIENTO), 3);
		String dPunExp = verificarLongitud(getParametrosCdc(xmlDocument, PUNTO_EXPEDICION), 3);
		String dNumDoc = getdNumDoc(xmlDocument);
		String iTipCont = verificarLongitud(getParametrosCdc(xmlDocument, TIPO_CONTRIBUYENTE), 1);
		String dFeEmiDE = getFechaEmision(xmlDocument, FECHA_EMISION);
		String iTipEmi = verificarLongitud(getParametrosCdc(xmlDocument, TIPO_EMISION), 1);
		String dCodSeg = verificarLongitud(getParametrosCdc(xmlDocument, CODIGO_SEGURIDAD), 9);
		return iTiDE + dRucEm + dDVEmi + dEst + dPunExp + dNumDoc + iTipCont + dFeEmiDE + iTipEmi + dCodSeg;
		
	}

	private String verificarLongitud(String valor, int longitud) 
			throws SIFENException {
		if (valor.length() > longitud) {	
			throw new SIFENException("Error longitud del parametro "
					+ "(" + valor +") mayor al permitido (" + longitud + ").");
		}
		return valor;
	}

	private String getFechaEmision(Document xmlDocument, String codFechaEmision) 
			throws SIFENException {
		String dFeEmiDE = getParametrosCdc(xmlDocument, codFechaEmision);
		String[] dFeEmiDEArray = dFeEmiDE.split("T");
		return dFeEmiDEArray[0].replace("-", "");
	}

	/**
	 * Obtener parametros para el CDC
	 * @param xmlDocument
	 * @param campo
	 * @return valorElemento
	 * @throws SIFENException
	 */
	private String getParametrosCdc(Document xmlDocument, String campo) throws SIFENException{
		String valorElemento = null;
		try {
			valorElemento = obtenerValorElemento(xmlDocument, campo); 
			if(valorElemento == null || valorElemento.isEmpty()) {
				throw new XMLReadException(ERROR_LECTURA_XML);
			}
		}catch (Exception e) {
			throw new SIFENException(ERROR_MESSAGE + campo);
		}
		return valorElemento;
	}
	/**
	 * obtenemos número de documento
	 * @param xmlDocument
	 * @return dNumDoc
	 * @throws SIFENException
	 */
	private String getdNumDoc(Document xmlDocument) throws SIFENException {
		String dNumDoc = getParametrosCdc(xmlDocument, NRO_DOCUMENTO); 
		try (Formatter fmt = new Formatter()){
			if (dNumDoc.length() < 7) {		
				fmt.format("%07d",new Integer(dNumDoc));
				dNumDoc = fmt.toString();
			}
		}catch (Exception e) {
			throw new SIFENException(ERROR_MESSAGE + NRO_DOCUMENTO);
		}
		
		return dNumDoc;
	}

	/**
	 * Obtenemos el ruc emisor
	 * @param doc
	 * @return dRucEm
	 * @throws SIFENException
	 */
	private String getRucEmisor(Document doc) throws SIFENException{
		String dRucEm = getParametrosCdc(doc, RUC_EMISOR); 
		try (Formatter fmt = new Formatter()){
			if (dRucEm.length() < 8) {		
				fmt.format("%08d", new Integer(dRucEm));
				dRucEm = fmt.toString();
			}
		}catch (Exception e) {
			throw new SIFENException(ERROR_MESSAGE + RUC_EMISOR);
		}
		return dRucEm;
	}
	
	/**
	 * Obtenemos el Tipo de documento
	 * @param doc
	 * @return iTiDE
	 * @throws SIFENException
	 */
	private String getTipoDocumento(Document doc) throws SIFENException{	
		String iTiDE = getParametrosCdc(doc, TIPO_DOCUMENTO); 	
		try (Formatter fmt = new Formatter()){
			if (iTiDE.length() == 1) {
				fmt.format("%02d",new Integer(iTiDE));
				iTiDE = fmt.toString();
			}
		} catch (Exception e) {
			throw new AttributeNotFoundException(ERROR_MESSAGE + TIPO_DOCUMENTO);
		}	
		return iTiDE;
	}
	
	/**
	 * Obtenemos el valor del elemento a partir del tag
	 * @param xmlDocument
	 * @param tag
	 * @return
	 */
	private String obtenerValorElemento(Document xmlDocument, String tag) {
		
		NodeList tagXML = xmlDocument.getElementsByTagNameNS("*",tag);
		
		if(tagXML.getLength() > 0 && tagXML.item(0) != null)			
			return tagXML.item(0).getTextContent();
		else if(xmlDocument.getElementsByTagName(tag).item(0) != null)			
			return xmlDocument.getElementsByTagName(tag).item(0).getTextContent();
		else
			return null;
	}
	
	/**
	 * Obtenemos el elemento a partir del tag
	 * @param xmlDocument
	 * @param tag
	 * @return
	 */
	private Element getElement(Document xmlDocument, String tag) {
		return (Element) xmlDocument.getElementsByTagName(tag).item(0);
	}
	
	/**
	 * convierte a hexadecimal
	 * @param arg
	 * @return valor
	 */
	private static String toHex(String arg) {
	    return String.format("%40x", new BigInteger(1, arg.getBytes())).trim();
	}
	
	/**
	 * Envio de Correo Electrónico al receptor
	 * @param xDEXml
	 * @param cdc
	 * @param dEmailRec
	 * @throws SIFENException
	 * @throws TransformerException
	 */
	private void enviarEmail(Document xDEXml, String cdc, String dEmailRec) 
			throws SIFENException, TransformerException {
	
		String emailRecCon = "";
		boolean enviarCC = false;
		String asunto = "DTE - " + cdc;
		String texto = "Se ha emitido el Documento Tributario Electrónico con "
				+ "CDC: " + cdc + ". Se Adjunta el KuDE y el DTE en formato XML.";	
	
		EmailUtils emailUtilsAttach= new EmailUtils();
					
		//--kude
		EmailUtils.Attachment attachmentKuDE = 
				emailUtilsAttach.new Attachment(
						getKuDE(cdc), 
						cdc + ".pdf", 
						"pdf");
		
		//--document XML
		EmailUtils.Attachment attachmentXde = 
				emailUtilsAttach.new Attachment(
						XmlUtils.documentToString(xDEXml).getBytes(StandardCharsets.UTF_8), 
						cdc + ".xml", 
						"xml");
		
		EmailUtils.Attachment[] attachments = {attachmentKuDE, attachmentXde};
		
		if(enviarCC && !dEmailRec.isEmpty()) {
			emailUtils.sendMailWithAttachment(dEmailRec,emailRecCon, asunto, texto, attachments);
			logMessage("Correo Enviado con copia to: {}; Cc: {}",dEmailRec,emailRecCon);
		}else if (enviarCC && dEmailRec.isEmpty()) {
			emailUtils.sendMailWithAttachment(emailRecCon, asunto, texto, attachments);
			logMessage("Correo Enviado solo al declarado to: {}",emailRecCon);
		}else if(!dEmailRec.isEmpty()) {
			emailUtils.sendMailWithAttachment(dEmailRec, asunto, texto, attachments);
			logMessage("Correo Enviado al del XML to: {}",dEmailRec);
		}else {
			if(LOGGER.isInfoEnabled())
				LOGGER.error("Correo no enviado: No se especificó una dirección de correo");
		}
		
	}
	
	private void logMessage(String msg, Object... args) {
		if(!LOGGER.isInfoEnabled())
			return;
		
		LOGGER.info(msg,args);
		
	}

	private String parametroDetalleTipoDocumentoElectronico(String codigo) {
		//List<EkParametro> parametro = parametrosServices.getParametrosPorDominio("TIPO_DOCUMENTO_ELECTRONICO");
		String descripcion = "TIPO_DOCUMENTO_ELECTRONICO";
		return descripcion;
	}
	
	private static final String FILTROS = "filtros";
	private static final String INICIO = "inicio";

	
	private Map<String, Object> formatearParam(Map<String, Object> parametros, String rucReceptor) {
		if(parametros.containsKey(FILTROS)) {
			String filtros = (String) parametros.get(FILTROS);
			filtros = filtros == null || filtros.isEmpty() ? "{" : filtros.substring(0, filtros.length() - 1);
			if(!filtros.contains("rucReceptor")) {
				filtros += ",\"rucReceptor\":\""+rucReceptor+"\"}";
			}	
			else {
				filtros += "}";
			}
			
			parametros.put(FILTROS, filtros);
		} else {
			String filtros = "{\"rucReceptor\":\""+rucReceptor+"\"}";
			parametros.put(FILTROS, filtros);
		}
		return parametros;
	}
	
	private void validateParam(Map<String, Object> param, String claveObligatoria) throws SIFENException {
		if (param == null || !param.containsKey(claveObligatoria)) {
			throw new SIFENException("Parametros incorrectos [falta: " + claveObligatoria + "]");
		}
	}
	
	private String obtenerDigestValue(Document xmlDocument) {
		   
		String digest = obtenerValorElemento(xmlDocument, DIGEST);
    	if(digest != null && !digest.isEmpty()) {
    		return digest;
    	}
	
		String digestWithPrefix = obtenerValorElemento(xmlDocument, DIGEST_PREFIX);
		if(digestWithPrefix != null && !digestWithPrefix.isEmpty()) {
    		return digestWithPrefix;
    	}
    	return "";
	}
	
	/**
     * Método para obtener la los parámetros de una {URL}
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf('=');
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            
            queryPairs.computeIfAbsent(key, k -> new LinkedList<>());
            
            final String value = idx > 0 && pair.length() > idx + 1
                    ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                    : null;
            queryPairs.get(key).add(value);
        }
        return queryPairs;
    }
    
    private boolean hasCSC(String ruc) {
        int idCsc = 1;       
        String key = "sifen:ruc:" + ruc + ":idcsc:" + Integer.toString(idCsc);
        //Jedis jedis = jedisPool.getResource();
        String secret = ""; //jedis.hget(key, "csc");
        return secret != null && !secret.isEmpty();
    }

	@Override
	public Long guardarDocumentoElectronicoGratuito(Map<String, Object> parametros, Long usuario)
			throws SIFENException, TransformerException, IOException, SAXException,
			ParserConfigurationException, SIFENGuardarException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countEventosReceptor(Map<String, Object> parametros) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResponseEntity<Object> consultarDE(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
