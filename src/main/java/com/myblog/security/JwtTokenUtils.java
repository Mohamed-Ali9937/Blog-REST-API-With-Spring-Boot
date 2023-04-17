package com.myblog.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.myblog.exception.BlogApiException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenUtils {
	
	@Value("${tokenSecert}")
	private String tokenSecert;
	
	@Value("${tokenExpirationDate}")
	private long expirationDate;
	
	public String generateJwtToken(String userName) {
		String token = Jwts.builder()
				.setSubject(userName)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationDate))
				.signWith(key(), SignatureAlgorithm.HS256)
				.compact();
		
		return token;
	}
	
	public Claims extractAllClaims(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		return claims;		
	}
	
	public <T> T extractClaim(String token, Function<Claims,  T>  calimsGenerator) {
		Claims claims = extractAllClaims(token);
		
		return calimsGenerator.apply(claims);
	}
	
	public String getUsername(String token) {
		return extractClaim(token, claims -> claims.getSubject());
	}
	
	public boolean isTokenValid(String token) {
		try {
		Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parse(token);
		
		return true;
		}catch(MalformedJwtException | SignatureException | ExpiredJwtException | IllegalArgumentException ex) {
			throw new BlogApiException(ex.getMessage());
		}
	}
	
	public Key key() {
		
		Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecert));
		
		return key;
	}
}
