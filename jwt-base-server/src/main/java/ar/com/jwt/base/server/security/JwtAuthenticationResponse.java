package ar.com.jwt.base.server.security;

import java.io.Serializable;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.security<br/>
 * 
 * <b>Class:</b> JwtAuthenticationResponse<br/>
 * 
 * <b>Description:</b> Simple POJO object containing generated token after successfull authentication.<br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Christian C. Carapezza<br/>
 * <br/>
 */
public class JwtAuthenticationResponse implements Serializable {

	// Generated serial id.
	private static final long serialVersionUID = 1250166508152483573L;

	// The generated token.
	private final String token;

	/**
	 * 
	 * Default constructor using fields.
	 * @param token after succesful login.
	 */
	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
 
}
