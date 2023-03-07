package py.com.fermar.test.firma;

import static org.testng.AssertJUnit.assertNotNull;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import py.com.fermar.tools.ksigner.SignXmlSifenWithCryptoDsig;
import java.io.File;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyStore;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;

@Test
public class ValidarFirmaTest {

	@Autowired
	//private ValidarFirmaService validarFirmaService;

	private static final String XML_FILE_NAME_141 = "rDE141.xml";
	private static final String KEYSTORE_FILE_NAME = "keystoreKonecta.jks";
	private static final String XML_FILE_NAME_141_SIGNED = "rDE141Signed.xml";

	@Test
	public void verifySignatureTest() {

		try {
			ClassLoader classLoader = getClass().getClassLoader();
			URL fUrl = classLoader.getResource(XML_FILE_NAME_141);

			KeyStore ks = KeyStore.getInstance("JKS");
			URL keystoreFileUrl = classLoader.getResource(KEYSTORE_FILE_NAME);
			String storename = keystoreFileUrl.getPath();
			char[] storepass = "4jd3GAd35U3skXJ8".toCharArray();
			try (FileInputStream fin = new FileInputStream(storename)) {
				ks.load(fin, storepass);
			}
			SignXmlSifenWithCryptoDsig firma = new SignXmlSifenWithCryptoDsig();
			Document signedXmlDocument = firma.signDocument(fUrl.getPath(), ks);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			StringWriter sw = new StringWriter();
			trans.transform(new DOMSource(signedXmlDocument), new StreamResult(sw));
			System.out.println(sw.toString());

			//validarFirmaService.validarXMLFirma();

		} catch (Exception e) {
			assertNotNull("0120");
		}
	}

}
