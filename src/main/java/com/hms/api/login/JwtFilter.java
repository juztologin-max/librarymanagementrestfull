package com.hms.api.login;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final AuthenticationEntryPoint authenticationEntryPoint;

	public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService,
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer")) {
			String jws = authHeader.substring("Bearer ".length());
			try {
				jwtService.isExpired(jws);

			} catch (ExpiredJwtException ex) {
				authenticationEntryPoint.commence(request, response, new BadCredentialsException("Expired token"));
				return;
			} catch (Exception ex) {
				authenticationEntryPoint.commence(request, response, new BadCredentialsException("Invalid token"));
				return;
			}
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String username = jwtService.extractUsername(jws);
				UserDetails user = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
						.authenticated(user, null, user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		}

		filterChain.doFilter(request, response);

	}

}
