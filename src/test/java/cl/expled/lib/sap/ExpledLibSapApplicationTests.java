package cl.expled.lib.sap;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cl.expled.lib.sap.controller.SapController;

@SpringBootTest
class ExpledLibSapApplicationTests {
	@Test
	void contextLoads0() throws IOException {
		
	}
	//@Test
	void contextLoads() throws IOException {
		System.out.println("SapController");
		SapController sap = new SapController()
			/*.setJCO_ASHOST("/H/200.54.27.10/H/192.168.10.98")
			.setJCO_CLIENT("300")
			.setJCO_PASSWD("Invertec@2019.")
			.setJCO_USER("WULTU_MOVIL")
			.setJCO_SYSNR("00")
			.setJCO_LANG("ES");*/;
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
