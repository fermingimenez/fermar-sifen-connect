package py.com.fermar;

//import static org.testng.AssertJUnit.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import py.com.fermar.tools.ksigner.SignXmlSifenWithCryptoDsig;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Respuesta extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1532030735873447894L;

	/**
	 * Constructor of the object.
	 */
	public Respuesta() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String firmar = (String) request.getParameter("firmar");
		String rDE = (String) request.getParameter("rDE");

		String KEYSTORE_FILE_NAME = "keystoreKonecta.jks";
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			URL fUrl = classLoader.getResource(rDE);

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
			//assertNotNull("0120");
			e.printStackTrace();
		}
		
		//Elimino los \n y \r para evitar errores al generar el hash
		//docXml = docXml.replaceAll("\n", "").replaceAll("\r", "");
		

	}

}
