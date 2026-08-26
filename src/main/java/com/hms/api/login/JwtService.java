package com.hms.api.login;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	// 32 characters
	// openssl rand -base64 32

	private final String SECRET_KEY;
	private final long SECRET_KEY_EXPIRATION;

	public JwtService(@Value("${JWT_SECRET_KEY}") String key, @Value("${JWT_SECRET_KEY_EXPIRATION}") long expiration) {
		this.SECRET_KEY = key;
		this.SECRET_KEY_EXPIRATION = expiration;
	}

	public String generateToken(String username) {
		return Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + SECRET_KEY_EXPIRATION)).signWith(getKey()).compact();

	}

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
	}

	public String extractUsername(String jws) {
		return extractPayload(jws).getSubject();
	}

	public boolean isExpired(String jws) {
		return extractPayload(jws).getExpiration().before(new Date());
	}

	public Claims extractPayload(String jws) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jws).getPayload();
	}

}
