package ar.com.jwt.base.server.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 
 * <b>Project/Module:</b> jwt-base-server<br/>
 * 
 * <b>Package:</b> ar.com.jwt.base.server.security.util<br/>
 * 
 * <b>Class:</b> JwtTokenUtil<br/>
 * 
 * <b>Description:</b> Util Helper class that manipulates different aspects of a json web token.<br/>
 * 
 * <b>Creation Date:</b> May 23, 2016<br/>
 * 
 * @author Juan P. Holder (juanholder@gmail.com) <br/>
 * <br/>
 */
@Component
public class JwtTokenUtil implements Serializable {

	/*
	 * Generated serial id.
	 */
	private static final long serialVersionUID = -3301605591108950415L;

	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_AUDIENCE = "audience";
	private static final String CLAIM_KEY_CREATED = "created";

	private static final String AUDIENCE_UNKNOWN = "unknown";
	private static final String AUDIENCE_WEB = "web";
	private static final String AUDIENCE_MOBILE = "mobile";
	private static final String AUDIENCE_TABLET = "tablet";

	/*
	 * Token params.
	 */
	@Value("${conf.properties.secret}")
	private String secret;
	@Value("${conf.properties.expiration}")
	private Long expiration;

	/**
	 * 
	 * Default constructor.
	 */
	public JwtTokenUtil() {
		super();
	}

	/**
	 * Extracts the username from the token.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the username or null if not found.
	 */
	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	/**
	 * Extracts the generated date from the token.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the generated date or null if not found.
	 */
	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	/**
	 * Extracts the expiration date from the token.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the expiration date or null if not found.
	 */
	public Date getExpirationDateFromToken(String token) {
		Date expirationDate;
		try {
			final Claims claims = getClaimsFromToken(token);
			expirationDate = claims.getExpiration();
		} catch (Exception e) {
			expirationDate = null;
		}
		return expirationDate;
	}

	/**
	 * Extracts the audience from the token.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the username or null if not found.
	 */
	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = getClaimsFromToken(token);
			audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	/**
	 * Extracks the claims from token.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the username or null if not found.
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/**
	 * Generates an expiration date since the current expiration amount.
	 * 
	 * @return the date to be expired.
	 */
	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * Checks if the token has expired.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the check result.
	 */
	private Boolean isTokenExpired(String token) {
		final Date expirationDate = getExpirationDateFromToken(token);
		return expirationDate.before(new Date());
	}

	/**
	 * Checks if the token was created before the last pass reset time.
	 * 
	 * @param created
	 *            time.
	 * @param lastPasswordReset
	 *            time.
	 * @return true if it was created before the last pass reset time.
	 */
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return lastPasswordReset != null && created.before(lastPasswordReset);
	}

	/**
	 * Get the audience due a device.
	 * 
	 * @param device
	 *            to be analised.
	 * @return the audience.
	 */
	private String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		if (device.isNormal()) {
			audience = AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	/**
	 * Checks if the token expiration is ignotable due the type of aoudience.
	 * 
	 * @param token
	 *            to be analised.
	 * @return the check result.
	 */
	private Boolean ignoreTokenExpiration(String token) {
		String audience = getAudienceFromToken(token);
		return AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience);
	}

	/**
	 * Generate a new token.
	 * 
	 * @param userDetails
	 *            of the user to generate the token.
	 * @param device
	 *            of the request sources.
	 * @return the generated token.
	 */
	public String generateToken(UserDetails userDetails, Device device) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims);
	}

	/**
	 * Generate a new token.
	 * 
	 * @param claims
	 *            to be used for creation.
	 * @return the new token.
	 */
	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Checks if a token ca be refreshed.
	 * 
	 * @param token
	 *            to be analised.
	 * @param lastPasswordReset
	 *            date.
	 * @return the result of the check.
	 */
	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	/**
	 * Refreshes the token returning a new one.
	 * 
	 * @param token
	 *            to be analised
	 * @return the new token.
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	/**
	 * Checks if the token is valid.
	 * 
	 * @param token
	 *            to be analised.
	 * @param userDetails
	 *            to be used to compare.
	 * @return the truth of validation.
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {

		final String username = getUsernameFromToken(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}