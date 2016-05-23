package ar.com.jwt.base.server.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.jwt.base.server.security.JwtAuthenticationRequest;
import ar.com.jwt.base.server.security.JwtAuthenticationResponse;
import ar.com.jwt.base.server.security.util.JwtTokenUtil;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.security.controller<br/>
 * 
 * <b>Class:</b> AuthenticationRestController<br/>
 * 
 * <b>Description:</b> Authentication REST controller.<br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Juan P. Holder (juanholder@gmail.com) <br/>
 * <br/>
 */
@RestController()
@RequestMapping("api/security")
@SuppressWarnings("rawtypes")
public class AuthenticationRestController {

	/*
	 * The token header name to be extracted.
	 */
	@Value("${conf.properties.authorisation.header}")
	private String tokenHeader;

	/*
	 * The spring security authentication manager.
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/*
	 * The token util.
	 */
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/*
	 * The user details service.
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	public AuthenticationRestController() {
		this.tokenHeader = "Authorization";
	}

	/**
	 * Returns an authentication token.
	 * 
	 * @param authenticationRequest
	 *            for user/pass extraction.
	 * @param device
	 *            of the request source.
	 * @return the new token.
	 * @throws AuthenticationException
	 *             if any error occurs.
	 */
	@RequestMapping(value = "auth", method = RequestMethod.POST)
	public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			Device device) {

		// Perform the security
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-security so we can generate token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails, device);

		// Return the token
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}

	/**
	 * Refresh a token.
	 * 
	 * @param request
	 *            to be analised to extract the token.
	 * @return the new token.
	 */
	@RequestMapping(value = "refresh", method = RequestMethod.GET)
	public ResponseEntity refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String refreshedToken = jwtTokenUtil.refreshToken(token);
		return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));

	}

}
