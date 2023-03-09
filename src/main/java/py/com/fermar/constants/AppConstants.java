package py.com.fermar.constants;

import java.util.ArrayList;
import java.util.List;

public abstract class AppConstants {
	
	public static final String CODIGOMENSAJE_SUCESO = "0200";

	public static final String SIFEN_CONFIG_FILENAME = "sifen.properties";
	
	public static final String EKUATIAI_CONFIG_FILENAME = "kuatiai.properties";
	
	public static final String CONF_SIFEN_BASEURL = "sifen.baseurl";
	public static final String CONF_SIFEN_DSNAME = "sifen.dsname";

	public static final String BODY_SIZE_LIMITER_HEAD = "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">\r\n<env:Header/>\r\n<env:Body>";
	public static final String BODY_SIZE_LIMITER_FOOT = "</env:Body>\r\n</env:Envelope>";

	public enum EstadosEnum {
		RECHAZO("R"), APROBADO("A"), NOTIFICACION("N");
		private final String value;

		private EstadosEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum EstadosLote {
		INGRESADO("I"), PROCESANDO("P"), FINALIZADO("F"), CANCELADO("C");
		private final String value;

		private EstadosLote(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum EstadosLotesDe {
		ERROR("E"), SUCESO("S");
		private final String value;

		private EstadosLotesDe(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum EstadosApiKey {
		ACTIVO("ACTIVO"), INACTIVO("INACTIVO");
		private final String value;

		private EstadosApiKey(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum PropietarioApiKey {
		SIFEN("SIFEN"), MARANGATU("MARANGATU");
		private final String value;

		private PropietarioApiKey(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum ProcesosLote {
		WORKER("W"), SCHEDULER("S");
		private final String value;

		private ProcesosLote(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	public enum ValidationCodesDE {
		XML_SCHEMA_NO_ENCONTRADO("0100", "Fallo de schema XML del área de datos"),
		CDC_DUPLICADO("1001", "CDC duplicado"),
		DOCUMENTO_DUCPLICADO("1002", "Documento electronico duplicado"),
		XML_MALFORMADO("0160", "XML malformado"),
		VERSION_WS_NO_SOPORTADO("0163", "Versión del formato del web service no soportado"),
		AMBIENTE_INVALIDO("0164","Ambiente de destino inválido"),
		AMBIENTE_EQUIVOCADO("0165","Envío al ambiente #destino informando el ambiente #informado como destino"),
		ID_YA_UTILIZADO("0166", "Ya fué recibido un mensaje de web service con dicho identificador de control"),
		HEADER_MSG_INEXISTENTE("0180", "Elemento deHeaderMsg inexistente en el Header del SOAP"),
		AUTORIZACION_EXITOSA("0260","Autorización del DE satisfactoria");

		private String codigo;
		private String mensaje;

		ValidationCodesDE(String codigo, String mensaje) {
			this.codigo = codigo;
			this.mensaje = mensaje;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getMensaje() {
			return mensaje;
		}
	}
	
	private static final String[] ABECEDARIO = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	public static final List<String> seriesValidas(){
		List<String> seriesValidas = new ArrayList<>();
		seriesValidas.add(null);
		
		// construyendo array de series validas
		for (int i = 0; i < ABECEDARIO.length; i++) {
			String letraAux = ABECEDARIO[i];
			for (int j = 0; j < ABECEDARIO.length; j++) {
				String serieV = letraAux.concat(ABECEDARIO[j]);
				seriesValidas.add(serieV);
			}
		}
		return seriesValidas;

	}
	
	public enum TipoDocumentoAsociado {
		ELECTRONICO("1", "Electrónico"),
		IMPRESO("2", "Impreso"),
		CONSTANCIA_ELECTRONICA("3", "Constancia Electrónica");
			
		private String codigo;
		private String descripcion;
	
		TipoDocumentoAsociado(String codigo, String descripcion) {
			this.codigo = codigo;
			this.descripcion = descripcion;
		}
	
		public String getCodigo() {
			return codigo;
		}
		
		public Integer getCodigoInt() {
			return Integer.valueOf(codigo);
		}
		
		public String getDescripcion() {
			return descripcion;
		}
	    
		/**
		 * Obtiene la descricpcion apartir del codigo
		 * @param codigo
		 * @return descripcion
		 */
		public static String getDescripcion(String codigo){
	        for(TipoDocumentoAsociado elem : values()){
	            if(codigo.equals(elem.getCodigo())){
	                return elem.getDescripcion();
	            }
	        }
	        return "";
	    }
	}

}
