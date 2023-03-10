package py.com.fermar.test.utils;

import static org.junit.Assert.assertTrue;
import java.math.BigInteger;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import py.com.fermar.test.TestSuiteBase;
import py.com.fermar.utils.Utils;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"file:src/test/resources/testApplicationContext.xml"})
//@Transactional
public class UtilsTest extends TestSuiteBase{

		
		@Test	
		public void calcularYComparar() {
			
			String valor = "0180094652900100100100012202303081000010001";
			Integer baseMax = 11;
			String digitoDeComparacion = "3";
			
			System.out.println(Utils.calcularYComparar(valor, baseMax, digitoDeComparacion));
			
			assertTrue(true);
			
		}
		
		private static String toHex(String arg) {
		    return String.format("%40x", new BigInteger(1, arg.getBytes())).trim();
		}
}
