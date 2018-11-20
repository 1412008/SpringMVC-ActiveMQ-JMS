package web.globalvars;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbConfig {
	
	@Value("${mongo.host}")
	private String HOST;
	
	@Value("${mongo.port}")
	private int PORT;
	
	@Value("${mongo.database}")
	private String DATABASE;

	public String getHOST() {
		return HOST;
	}

	public int getPORT() {
		return PORT;
	}

	public String getDATABASE() {
		return DATABASE;
	}
	
}
