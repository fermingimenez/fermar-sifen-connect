package py.com.fermar.test.qr;

import org.testng.annotations.Test;
import java.math.BigInteger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;


@Test
public class GenerarQRTest {

		private static final String QR_CODE_IMAGE_PATH_SHA1 = "./MyQRCodeSHA1.png";
		private static final String QR_CODE_IMAGE_PATH_SHA2 = "./MyQRCodeSHA2.png";
		private static final String URL = "https://ekuatia.set.gov.py/consultas-test/qr?";
		private static final String NVERSION = "nVersion";
		private static final String ID = "Id";
		private static final String DFEEMIDE = "dFeEmiDE";
		private static final String DRUCREC = "dRucRec";
		private static final String DNUMIDREC = "dNumIDRec";
		private static final String DTOTGRALOPE = "dTotGralOpe";
		private static final String DTOTIVA = "dTotIVA";
		private static final String CITEMS = "cItems";
		private static final String DIGESTVALUE = "DigestValue";
		private static final String IDCSC = "IdCSC";
		private static final String CHASHQR = "cHashQR";
		private static final String TAB = "\t ";
		
		@Test	
		public void TestQRGenerator() {
			
			String urlQR = URL;
			String nVersion = "141";
			String id = "01800025776001001900000022018102317200000000";
			String dFeEmiDE = "2018-10-23T09:13:09";
			String dRucRec = null;
			String dNumIDRec = "45700211";
			String dTotOpe = "5684203.00000000";
			String dTotIVA = "516746.00000000";
			String cItems = "1";
			String digestValue = "tPW+iKdUWg93lPLN/XIrSVbp8VjuxhWJGwWtlWhx0l0=";
			String idCSC = "0001";
			String cSC1 = "ABCD0000000000000000000000000000";
			String cSC2 = "EFGH0000000000000000000000000000";
			String cSC3 = "123456789";
			
			//Seleccionar el Código de Seguridad a utilizar
			int selectedCSC = 1;
			String cSC = "";
			
			if(selectedCSC == 1)
				cSC = cSC1;
			else if(selectedCSC == 2)
				cSC = cSC2;
			else
				cSC = cSC3;
			
			String dFeEmiDEHex = toHex(dFeEmiDE);
			String digestValueHex = toHex(digestValue);
			String hashHex;
			
			System.out.println("URL" + TAB + TAB + urlQR);
			System.out.println(NVERSION + TAB + nVersion);
			System.out.println(ID + TAB + TAB + id);
			System.out.println(DFEEMIDE + TAB + dFeEmiDE);
			
			if(dNumIDRec == null)
				System.out.println(DRUCREC + TAB + TAB + dRucRec);
			else
				System.out.println(DNUMIDREC + TAB + dNumIDRec);
			
			System.out.println(DTOTGRALOPE + TAB + dTotOpe);
			System.out.println(DTOTIVA + TAB + TAB + dTotIVA)   ;
			System.out.println(CITEMS + TAB + TAB + cItems);
			System.out.println(DIGESTVALUE + TAB + digestValue);
			System.out.println(IDCSC + TAB + TAB + idCSC);
			System.out.println("CSC" + TAB + TAB + cSC);
			System.out.println("");
			System.out.println("---------------------------------------");
							
			
			System.out.println("Valor Hexadecimal de dFeEmiDE: ");
			System.out.println("dFeEmiDEHex   :" + dFeEmiDEHex);
			System.out.println("");
			
			System.out.println("Valor Hexadecimal de DigestValue:");
			System.out.println("DigestValueHex:" + digestValueHex);
			System.out.println("");
			
			
			String msg = NVERSION + "=" 		+ nVersion				
					+ "&" + ID + "="			+ id
					+ "&" + DFEEMIDE + "=" 		+ dFeEmiDEHex;
					if(dNumIDRec == null)
						msg += "&" + DRUCREC + "=" + dRucRec;
					else
						msg += "&" + DNUMIDREC + "=" + dNumIDRec;
					
					msg += "&" + DTOTGRALOPE + "=" 	+ dTotOpe
					+ "&" + DTOTIVA + "="			+ dTotIVA
					+ "&" + CITEMS + "="			+ cItems
					+ "&" + DIGESTVALUE + "=" 		+ digestValueHex
					+ "&" + IDCSC + "=" 			+ idCSC;
					
			System.out.println("1. Datos de Ejemplo para generación de QR:");
			System.out.println(msg);
			System.out.println("");
			
			System.out.println("2. Adjuntar Código Secreto del Contribuyente:");
			System.out.println(msg + cSC);
			System.out.println("");
			
			System.out.println("3. Generar Hash de datos:");

			hashHex = new DigestUtils(MessageDigestAlgorithms.SHA_256).digestAsHex(msg + cSC);
							
			System.out.println("hashHex: " + hashHex);
			System.out.println("");
			
			System.out.println("4. URL de la Imagen del QR:");
			String qR1 = URL + msg + "&" +CHASHQR+ "=" + hashHex;
			
			System.out.println(qR1);
			System.out.println("");
			
			System.out.println("5. URL de la Imagen del QR para insertar en el XML");
			String qR2 = URL + msg.replace("&", "&amp;") + "&amp;" +CHASHQR+ "=" + hashHex;
					
			System.out.println(qR2);
			
			
			System.out.println("");
			/*
			try {
				//System.out.println("6. Generar imagen QR: " + QR_CODE_IMAGE_PATH_SHA2);
	            //QRCodeGenerator.generateQRCodeImage(qR1, 200, 200, QR_CODE_IMAGE_PATH_SHA2);
	            
	        } catch (WriterException e) {
	        	System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
	        } catch (IOException e) {
	        	System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
	        }
	        */
			
		}
		
		private static String toHex(String arg) {
		    return String.format("%40x", new BigInteger(1, arg.getBytes())).trim();
		}
	
}
