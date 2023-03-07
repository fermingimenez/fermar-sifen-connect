package py.com.fermar.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TestSuiteBase {
	

	@BeforeClass
	public static void setUpOnce() throws InterruptedException {
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		System.setProperty("javax.xml.transform.TransformerFactory","com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		//System.setProperty("JBOSS_HOME","/home/fermin/EAP-7.1.0");
	}

    @AfterClass
    public static void tearDown() {
        System.out.println("Finalizado Set de Pruebas");
    }
}
