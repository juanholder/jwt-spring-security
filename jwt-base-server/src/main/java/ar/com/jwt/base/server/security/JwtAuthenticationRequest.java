package ar.com.jwt.base.server.security;

import java.io.Serializable;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.security<br/>
 * 
 * <b>Class:</b> JwtAuthenticationRequest<br/>
 * 
 * <b>Description:</b> Simple POJO object containing the user/pass credential for authentication.<br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Juan P. Holder (juanholder@gmail.com) <br/>
 * <br/>
 */
public class JwtAuthenticationRequest implements Serializable {

	// The generated serial id.
	private static final long serialVersionUID = -8445943548965154778L;

	// Credential input.
	private String username;
	private String password;

	/**
	 * 
	 * Default constructor.
	 */
	public JwtAuthenticationRequest() {
		super();
	}

	/**
	 * 
	 * Default constructor using files.
	 * 
	 * @param username
	 *            to be logged in.
	 * @param password
	 *            to be logged in.
	 */
	public JwtAuthenticationRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
