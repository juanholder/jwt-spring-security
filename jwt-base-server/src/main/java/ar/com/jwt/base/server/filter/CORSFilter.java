package ar.com.jwt.base.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.filter<br/>
 * 
 * <b>Class:</b> CORSFilter<br/>
 * 
 * <b>Description:</b> Http filter that allows cross domain access.<br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Juan P. Holder (juanholder@gmail.com) <br/>
 * <br/>
 */
public class CORSFilter extends OncePerRequestFilter {

	/*
	 * Static identifiers for methods and headers.
	 */
	private static final String HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
	private static final String REQUEST_METHOD = "OPTIONS";
	private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String ALLOW_METHODS = "GET, POST, PUT, DELETE";
	private static final String HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	private static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ALLOW_HEADERS = "Content-Type, x-http-method-override, username";

	/***
	 * {@inheritDoc}
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getHeader(HEADER_ACCESS_CONTROL_REQUEST_METHOD) != null
				&& REQUEST_METHOD.equals(request.getMethod())) {
			// CORS "pre-flight" request
			response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_METHODS, ALLOW_METHODS);
			// Set 60 min age.
			response.addHeader(HEADER_ACCESS_CONTROL_MAX_AGE, "3600");
			// IMPORTANT: here you shoud add ALL THE REQUIRED HTTP HEADERS TO BE USED.
			response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);
		} else {
			response.addHeader(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		}
		filterChain.doFilter(request, response);
	}

}
