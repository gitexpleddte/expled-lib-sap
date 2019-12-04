package cl.expled.lib.sap;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cl.expled.lib.sap.controller.SapController;

@SpringBootTest
class ExpledLibSapApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("SapController");
		/*SapController sap = new SapController()
			.setJCO_ASHOST("/H/200.54.27.10/H/192.168.10.98")
			.setJCO_CLIENT("300")
			.setJCO_PASSWD("Invertec@2019.")
			.setJCO_USER("WULTU_MOVIL")
			.setJCO_SYSNR("00")
			.setJCO_LANG("ES");
		if(!sap.connect()) {
			System.out.println();
		}
		String sJson ="{\"RFC\": \"ZMOV_40005\",\"getBase\":true}";
		JSONObject json = new JSONObject(sJson);
		System.out.println(sap.callRfc(json));*/
	}

}
