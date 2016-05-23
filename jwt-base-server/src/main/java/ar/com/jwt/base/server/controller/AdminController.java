package ar.com.jwt.base.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.controller<br/>
 * 
 * <b>Class:</b> AdminController<br/>
 * 
 * <b>Description:</b> Simple Controller that exposes Admin access methods.<br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Juan P. Holder (juanholder@gmail.com) <br/>
 * <br/>
 */
@RestController
@RequestMapping("api/admin")
@SuppressWarnings("rawtypes")
public class AdminController {

	/**
	 * Responds a raw message giving the greetings.
	 * 
	 * @return the message as a ResponseEntity.
	 */
	@RequestMapping(value = "myAdminMethod", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getProtectedGreeting() {
		return ResponseEntity.ok("Greetings from ADMIN protected method!");
	}

}
