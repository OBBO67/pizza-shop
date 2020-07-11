package com.pizzashop.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenVerifierFilter extends OncePerRequestFilter {
	
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	
	public JwtTokenVerifierFilter(JwtConfig jwtConfig, SecretKey secretKey) {
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// get the jwt from the header
		String authHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
		
		// invalid token if it doesn't exist or prefix doesn't match - request rejected
		if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
			// pass onto next filter
			filterChain.doFilter(request, response);
			// return without authenticating the user
			return;
		}
		
		// remove prefix from jwt
		String token = authHeader.replace(jwtConfig.getTokenPrefix(), "");
		
		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder()
										.setSigningKey(secretKey)
										.build()
										.parseClaimsJws(token);
			
			Claims body = claimsJws.getBody();
			
			String username = body.getSubject();
			
			@SuppressWarnings("unchecked")
			List<Map<String, String>> authorities = 
				(List<Map<String, String>>)	body.get("authorities");
			
			Set<SimpleGrantedAuthority> simpleGrantedAuths = authorities
																.stream()
																.map(m -> new SimpleGrantedAuthority(m.get("authority")))
																.collect(Collectors.toSet());
			
			Authentication auth = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuths);
			
			// authenticate the user
			SecurityContextHolder.getContext().setAuthentication(auth);
			
		} catch (JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
		}
		
		// pass onto next filter
		filterChain.doFilter(request, response);
	}
	
	/**
	 * Used for custom filtering. Return true to avoid filtering for a certain request.
	 */
//	@Override
//	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//		String requestUrl = request.getRequestURL().toString();
//		log.info("Request URL: " + requestUrl);
//		return false;
//	}

}
