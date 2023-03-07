package py.com.fermar.services.impl;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.zxing.WriterException;
import py.com.fermar.exception.SIFENException;
import py.com.fermar.services.UtilsService;
import py.com.fermar.tools.ksigner.SignXmlSifenWithCryptoDsig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 */
@Service
public class UtilsServiceImpl implements UtilsService {

	@Autowired
	private ResourceLoader resource;

	@Override
	public ResponseEntity<Object> firmar(Map<String, Object> param) throws SIFENException {

		String KEYSTORE_FILE_NAME = "LCBA-DOCUMENTA.p12";
		StringWriter sw = new StringWriter();

		try {
			//KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore ks = KeyStore.getInstance("PKCS12");
			String storename = resource.getResource("/WEB-INF/resources/" + KEYSTORE_FILE_NAME).getFile().getPath();
			//char[] storepass = "4jd3GAd35U3skXJ8".toCharArray();
			char[] storepass = "qwerty".toCharArray();
			try (FileInputStream fin = new FileInputStream(storename)) {
				ks.load(fin, storepass);
			}

			String tempFolderPath = System.getProperty("jboss.server.temp.dir");
			String time = String.valueOf(System.currentTimeMillis());
			String outFile = tempFolderPath + "/" + "rDE" + time + ".xml";
			File f = new File(outFile);
			FileWriter fw = new FileWriter(outFile);

			Gson gson = new Gson();
			JsonElement element = gson.toJsonTree(param.get("datos"));
			JsonElement de = element.getAsJsonObject().get("rDE");

			fw.write(de.getAsString());
			fw.close();

			URL url = f.toURI().toURL();

			SignXmlSifenWithCryptoDsig firma = new SignXmlSifenWithCryptoDsig();
			Document signedXmlDocument = firma.signDocument(url.getPath(), ks);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(signedXmlDocument), new StreamResult(sw));

			String outFileFirmado = tempFolderPath + "/" + "rDEFirmado" + time + ".xml";
			File fFirmado = new File(outFileFirmado);
			FileWriter fwFirmado = new FileWriter(outFileFirmado);
			fwFirmado.write(sw.toString());
			fwFirmado.close();
			
			System.out.println(sw.toString());
		} catch (Exception e) {
			throw new SIFENException(e);
		}
		HashMap<String, String> retorno = new HashMap<String, String>();
		retorno.put("rDEFirmado", sw.toString());
		return new ResponseEntity<>(retorno, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> calcularQR(Map<String, Object> param) throws SIFENException {
		HashMap<String, Object> retorno = new HashMap<String, Object>();
		try {

			Gson gson = new Gson();
			JsonElement element = gson.toJsonTree(param.get("datos"));

			final String NVERSION = "nVersion";
			final String ID = "Id";
			final String DFEEMIDE = "dFeEmiDE";
			final String DRUCREC = "dRucRec";
			final String DNUMIDREC = "dNumIDRec";
			final String DTOTGRALOPE = "dTotGralOpe";
			final String DTOTIVA = "dTotIVA";
			final String CITEMS = "cItems";
			final String DIGESTVALUE = "DigestValue";
			final String IDCSC = "IdCSC";
			final String CHASHQR = "cHashQR";
			final String TAB = "\t ";
			final String CRLF = "\n";

			String optionID = element.getAsJsonObject().get("optionID").toString().replace("\"", "");
			String URL = element.getAsJsonObject().get("urlQR").toString().replace("\"", "");
			String urlQR = URL;

			String nVersion = element.getAsJsonObject().get("nVersion").toString().replace("\"", "");
			String id = element.getAsJsonObject().get("id").toString().replace("\"", "");
			String dFeEmiDE = element.getAsJsonObject().get("dFeEmiDE").toString().replace("\"", "");
			String dRucRec = null;
			String dNumIDRec = null;
			if ("ruc".equals(optionID)) {
				if (element.getAsJsonObject().get("dRucRec") == null) {
					throw new SIFENException("Debe informar RUC");
				}
				dRucRec = element.getAsJsonObject().get("dRucRec").toString().replace("\"", "");
			} else {
				if (element.getAsJsonObject().get("dNumIDRec") == null) {
					throw new SIFENException("Debe informar Cedula");
				}
				dNumIDRec = element.getAsJsonObject().get("dNumIDRec").toString().replace("\"", "");
			}
			String dTotOpe = element.getAsJsonObject().get("dTotOpe").toString().replace("\"", "");
			String dTotIVA = element.getAsJsonObject().get("dTotIVA").toString().replace("\"", "");
			;
			String cItems = element.getAsJsonObject().get("cItems").toString().replace("\"", "");
			String digestValue = element.getAsJsonObject().get("digestValue").toString().replace("\"", "");
			String idCSC = element.getAsJsonObject().get("idCSC").toString().replace("\"", "");
			String cSC1 = "ABCD0000000000000000000000000000";
			String cSC2 = "EFGH0000000000000000000000000000";
			// String cSC3 = element.getAsJsonObject().get("cSC3").toString();

			// Seleccionar el Código de Seguridad a utilizar
			int selectedCSC = Integer
					.valueOf(element.getAsJsonObject().get("selectedCSC").toString().replace("\"", ""));
			String cSC = "";

			if (selectedCSC == 1)
				cSC = cSC1;
			else if (selectedCSC == 2)
				cSC = cSC2;
			/*
			 * else cSC = cSC3;
			 */
			String dFeEmiDEHex = toHex(dFeEmiDE);
			String digestValueHex = toHex(digestValue);
			String hashHex;
			String respuestaQr = new String();
			respuestaQr += ("URL" + TAB + TAB + urlQR + CRLF);
			respuestaQr += (NVERSION + TAB + nVersion + CRLF);
			respuestaQr += (ID + TAB + TAB + id + CRLF);
			respuestaQr += (DFEEMIDE + TAB + dFeEmiDE + CRLF);

			if ("ruc".equals(optionID))
				respuestaQr += (DRUCREC + TAB + TAB + dRucRec + CRLF);
			else
				respuestaQr += (DNUMIDREC + TAB + dNumIDRec + CRLF);

			respuestaQr += (DTOTGRALOPE + TAB + dTotOpe + CRLF);
			respuestaQr += (DTOTIVA + TAB + TAB + dTotIVA + CRLF);
			respuestaQr += (CITEMS + TAB + TAB + cItems + CRLF);
			respuestaQr += (DIGESTVALUE + TAB + digestValue + CRLF);
			respuestaQr += (IDCSC + TAB + TAB + idCSC + CRLF);
			respuestaQr += ("CSC" + TAB + TAB + cSC + CRLF);
			respuestaQr += ("" + CRLF);
			respuestaQr += ("---------------------------------------" + CRLF);

			respuestaQr += ("Valor Hexadecimal de dFeEmiDE: " + CRLF);
			respuestaQr += ("dFeEmiDEHex   :" + dFeEmiDEHex + CRLF);
			respuestaQr += ("" + CRLF);

			respuestaQr += ("Valor Hexadecimal de DigestValue:" + CRLF);
			respuestaQr += ("DigestValueHex:" + digestValueHex + CRLF);
			respuestaQr += ("" + CRLF);

			String msg = NVERSION + "=" + nVersion + "&" + ID + "=" + id + "&" + DFEEMIDE + "=" + dFeEmiDEHex;
			if ("ruc".equals(optionID))
				msg += "&" + DRUCREC + "=" + dRucRec;
			else
				msg += "&" + DNUMIDREC + "=" + dNumIDRec;

			msg += "&" + DTOTGRALOPE + "=" + dTotOpe + "&" + DTOTIVA + "=" + dTotIVA + "&" + CITEMS + "=" + cItems + "&"
					+ DIGESTVALUE + "=" + digestValueHex + "&" + IDCSC + "=" + idCSC;

			respuestaQr += ("1. Datos de Ejemplo para generación de QR:" + CRLF);
			respuestaQr += (msg + CRLF);
			respuestaQr += ("" + CRLF);

			respuestaQr += ("2. Adjuntar Código Secreto del Contribuyente:" + CRLF);
			respuestaQr += (msg + cSC + CRLF);
			respuestaQr += ("" + CRLF);

			respuestaQr += ("3. Generar Hash de datos:" + CRLF);

			hashHex = new DigestUtils(MessageDigestAlgorithms.SHA_256).digestAsHex(msg + cSC);

			respuestaQr += ("hashHex: " + hashHex + CRLF);
			respuestaQr += ("" + CRLF);

			respuestaQr += ("4. URL de la Imagen del QR:" + CRLF);
			String qR1 = URL + msg + "&" + CHASHQR + "=" + hashHex;

			respuestaQr += (qR1 + CRLF);
			respuestaQr += ("" + CRLF);

			respuestaQr += ("5. URL de la Imagen del QR para insertar en el XML" + CRLF);
			String qR2 = URL + msg.replace("&", "&amp;") + "&amp;" + CHASHQR + "=" + hashHex;

			respuestaQr += (qR2 + CRLF);
			respuestaQr += ("" + CRLF);

			String QR_CODE_IMAGE_PATH_SHA2 = null;
			try {
				String tempFolderPath = System.getProperty("jboss.server.temp.dir");
				QR_CODE_IMAGE_PATH_SHA2 = tempFolderPath + "/" + "qr" + System.currentTimeMillis() + ".png";
				System.out.println("6. Generar imagen QR: " + QR_CODE_IMAGE_PATH_SHA2);
				QRCodeGenerator.generateQRCodeImage(qR1, 200, 200, QR_CODE_IMAGE_PATH_SHA2);
			} catch (WriterException e) {
				String error = "Could not generate QR Code, WriterException :: " + e.getMessage();
				throw new SIFENException(error);
			} catch (IOException e) {
				String error = "Could not generate QR Code, IOException :: " + e.getMessage();
				throw new SIFENException(error);
			}
			Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH_SHA2);
			InputStream in = Files.newInputStream(path);
			//byte[] QR_CODE_IMAGE_PATH_SHA2_PNG = IOUtils.toByteArray(in);

			String  QR_CODE_IMAGE_PATH_SHA2_PNG = Base64.encodeBase64String(IOUtils.toByteArray(in));
		    byte[] png = Base64.decodeBase64(QR_CODE_IMAGE_PATH_SHA2_PNG);
		        
			retorno.put("QR_CODE_IMAGE_PATH_SHA2", QR_CODE_IMAGE_PATH_SHA2);
			retorno.put("QR_CODE_IMAGE_PATH_SHA2_PNG", png);
			retorno.put("QRLog", respuestaQr);
			retorno.put("QRGenerado", qR2);
		} catch (Exception e) {
		//	throw new SIFENException(e);
		}
		return new ResponseEntity<>(retorno, HttpStatus.OK);
	}

	private static String toHex(String arg) {
		return String.format("%40x", new BigInteger(1, arg.getBytes())).trim();
	}

}
