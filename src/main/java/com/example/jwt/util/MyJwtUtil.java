package com.example.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class MyJwtUtil implements Serializer {
	// 5 hours in milliseconds -> 5 hrs * 60 mins * 60 secs * 1000 milliseconds
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

	private SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	@Override
	public byte[] serialize (Object o) throws SerializationException {
		return new byte[0];
	}

	// method to generate jwt token
	public String generateToken (UserDetails userDetails) {
		// claims contains the required info which we want to insert in our jwt token
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userDetails.getUsername());
		claims.put("password", userDetails.getPassword());

		return doGenerateToken(claims, userDetails.getUsername());
	}

//	*************** UTILITY METHODS ********************

	// utility method used by generateToken()
	private String doGenerateToken (Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject)
		 .setIssuedAt(new Date(System.currentTimeMillis()))
		 .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
		 .signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public String getUsernameFromToken (String token) {
		return getClaimFromToken(token, Claims :: getSubject);
	}

	public Date getExpirationDateFromToken (String token) {
		return getClaimFromToken(token, Claims :: getExpiration);
	}

	public <T> T getClaimFromToken (String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken (String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired (String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Boolean validateToken (String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
