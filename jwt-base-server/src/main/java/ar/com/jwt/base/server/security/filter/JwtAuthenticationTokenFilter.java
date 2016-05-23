package ar.com.jwt.base.server.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import ar.com.jwt.base.server.security.util.JwtTokenUtil;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.security.filter<br/>
 * 
 * <b>Class:</b> JwtAuthenticationTokenFilter<br/>
 * 
 * <b>Description:</b> Spring security UsernamePasswordAuthenticationFilter for authentication. <br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Juan P. Holder (juanholder@gmail.com) <br/>
 * <br/>
 */
public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	/*
	 * User detail service to load user details.
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * The JWT util for creating and manipulating tokens.
	 */
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/*
	 * Tocken header name.
	 */
	private String tokenHeader;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		// Try to retrieve the username from the http request.
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String authToken = httpRequest.getHeader(this.tokenHeader);
		String username = jwtTokenUtil.getUsernameFromToken(authToken);

		// If request do have a username, and there is no spring security context asociated.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// try to retrieve the userDetails (user data).
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			// Checks the given token agains the userDetails retrieved.
			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				// Do the spring security stuff.
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * @return the tokenHeader
	 */
	public String getTokenHeader() {
		return tokenHeader;
	}

	/**
	 * @param tokenHeader
	 *            the tokenHeader to set
	 */
	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}
}