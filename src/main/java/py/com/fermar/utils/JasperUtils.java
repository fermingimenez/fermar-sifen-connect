package py.com.fermar.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;


public abstract class JasperUtils {


	public static final String REPORT_KUDE = "ekuatia_kude.jasper";
	public static final String REPORT_KUDE_FE  = "ekuatia_kude_fe.jasper";
	public static final String REPORT_KUDE_NC_ND  = "ekuatia_kude_nc_nd.jasper";
	public static final String REPORT_KUDE_AFE = "ekuatia_kude_afe.jasper";
	public static final String REPORT_KUDE_BRS = "ekuatia_kude_bre.jasper";
	public static final String REPORT_KUDE_BVE = "ekuatia_kude_bve.jasper";
	public static final String REPORT_KUDE_NRE = "ekuatia_kude_nre.jasper";

	private static final String MARCA_DE_AGUA = "P_MARCA_DE_AGUA";
	

	public static byte[] generarKuDE(String reportFileName, Document document,
			Connection connection, boolean isMarcaAgua) throws JRException, IOException {

		JasperReport jasperReport = JasperUtils.getJasperReportFromFileName(reportFileName);
		jasperReport.setProperty("net.sf.jasperreports.default.pdf.font.name", "DejaVuSans.ttf");
		return pdf(fill(jasperReport, document, connection, isMarcaAgua));
	}

	public static byte[] generarKuDE(String reportFileName, String cdc)
			throws JRException, IOException {

		JasperReport jasperReport = JasperUtils.getJasperReportFromFileName(reportFileName);
		return pdf(fillCDC(jasperReport, cdc));
	}

	private static JasperReport getJasperReportFromFileName(String reportFileName)
			throws JRException, IOException {
		return (JasperReport) JRLoader.loadObject(new ClassPathResource(reportFileName).getFile());
	}

	private static JasperPrint fillCDC(JasperReport jasperReport, String cdc) throws JRException {

		Map<String, Object> params = new HashMap<>();

		Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream(cdc + ".xml"));

		params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);

		return JasperFillManager.fillReport(jasperReport, params);
	}

	private static JasperPrint fill(JasperReport jasperReport, Document document,
			Connection connection, boolean isMarcaAgua) throws JRException {

		Map<String, Object> params = new HashMap<>();

		Document document2;
		try {
			document2 = JRXmlUtils.parse(documentToInputStream(document));
		} catch (TransformerException | TransformerFactoryConfigurationError e) {
			return null;
		}

		params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document2);
		params.put(MARCA_DE_AGUA, isMarcaAgua ? 1 : 0);

		return JasperFillManager.fillReport(jasperReport, params, connection);
	}

	private static InputStream documentToInputStream(Document document)
			throws TransformerException, TransformerFactoryConfigurationError {
		System.setProperty("javax.xml.transform.TransformerFactory","com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = new DOMSource(document);
		Result outputTarget = new StreamResult(outputStream);

		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		Transformer transformer = tf.newTransformer();
		transformer.transform(xmlSource, outputTarget);

		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	/**
	 *
	 */
	private static byte[] pdf(JasperPrint jasperPrint) throws JRException {
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
}
