package cl.expled.lib.sap;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sap.conn.jco.ext.DestinationDataProvider;

import cl.expled.lib.sap.controller.SapController;

@SpringBootTest
class ExpledLibSapApplicationTests {
	@Test
	void contextLoads0() throws IOException {
		Properties connectProperties = new Properties();
		/*#sap qas
		movilidad.sap.JCO_ASHOST = /H/200.54.27.10/H/10.60.1.22
		movilidad.sap.JCO_SYSNR = 00
		movilidad.sap.JCO_CLIENT = 400
		movilidad.sap.JCO_USER = MOVILIDAD2
		movilidad.sap.JCO_PASSWD = Inicio02
		movilidad.sap.JCO_LANG = ES*/;
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "/H/200.54.27.10/H/10.60.1.22");
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "00");
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "400");
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, "MOVILIDAD2");
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "Inicio01");
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "ES");
		connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "60000");
		connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "0");
		System.out.println("SapController");
		SapController sap = new SapController();
		sap.setCONNECION_ID("MOVILIDAD2");
		sap.connect(connectProperties);
		String sJson ="{\"RFC\": \"ZMOV_10002\",\"getBase\":true}";
		JSONObject json = new JSONObject(sJson);
		System.out.println(sap.callRfc(json));
	}
	//@Test
	void contextLoads() throws IOException {
		System.out.println("SapController");
		SapController sap = new SapController();
			
		String ps="jco.client.lang=ES, jco.client.client=300, jco.client.passwd=Invertec@2019., jco.destination.expiration_time=60000, jco.client.user=WULTU_MOVIL, jco.client.sysnr=00, jco.client.ashost=/H/200.54.27.10/H/192.168.10.98"; 
		
		Properties p = new Properties();
		p.load(new StringReader(ps.replaceAll(", ", "\n")));
		System.out.println(p);
		if(!sap.connect(p)) {
			System.out.println();
		}
		String sJson ="{\"RFC\": \"ZMOV_40005\",\"getBase\":true}";
		JSONObject json = new JSONObject(sJson);
		System.out.println(sap.callRfc(json));
	}
	
	

}
