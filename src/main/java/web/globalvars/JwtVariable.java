package web.globalvars;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtVariable {
	@Value("${jwt.header}")
	private String HEADER;

	@Value("${jwt.secret}")
	private String SECRET;

	@Value("${jwt.app_name}")
	private String APP_NAME;

	@Value("${jwt.expires_in}")
	private int EXPIRES_IN;

	public String getHEADER() {
		return HEADER;
	}

	public String getSECRET() {
		return SECRET;
	}

	public String getAPP_NAME() {
		return APP_NAME;
	}

	public int getEXPIRES_IN() {
		return EXPIRES_IN;
	}

}
