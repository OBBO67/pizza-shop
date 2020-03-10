package com.pizzashop.jwt;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzashop.auth.User;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Date;

/*
 * Used to verify the credentials of the user using their
 * username and password.
 */
@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter
	extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authManager;
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, 
			JwtConfig jwtConfig,
			SecretKey secretKey) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}
	
	/*
	 * Here we check if the user is authenticated i.e. here the user is attempting to
	 * login. If there credentials are valid then we issue them a JWT within the
	 * successfulAuthentication method.
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		log.info("Attempting authentication");
		
		try {
			UsernameAndPasswordAuthenticationRequest authRequest
											= new ObjectMapper().readValue(
													request.getInputStream(), 
													UsernameAndPasswordAuthenticationRequest.class);
			
			Authentication auth = new UsernamePasswordAuthenticationToken(
					authRequest.getUsername(), 
					authRequest.getPassword());
			
			// check username exists and password is correct
			Authentication authenticate = authManager.authenticate(auth);
			log.info(authenticate.getPrincipal().toString());
			
			if (authenticate.getPrincipal() instanceof User) {
				log.info("Found the user");
			}
			
			// Set SecurityContextHolder Authentication here?????
			SecurityContextHolder.getContext().setAuthentication(authenticate);
			
			return authenticate;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * This is invoked after the attemptAuthentication is successful. If the
	 * attemptAuthentication fails then this method is never executed.
	 * Here we want to create a JWT and send it to the client.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		log.info("Generating jwt");
		
		// generate a jwt
		String token = Jwts
						.builder()
						.setSubject(authResult.getName()) // the username
						.claim("authorities", authResult.getAuthorities())
						.setIssuedAt(new Date())
						.setExpiration(java.sql.Date.valueOf(
								LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
						.signWith(secretKey)
						.compact();
		
		// add the jwt to the response header
		response.addHeader(jwtConfig.getAuthorizationHeader(), 
					jwtConfig.getTokenPrefix() + token);
		
		if (authResult.getPrincipal() instanceof User) {
			log.info("User: " + authResult.toString());
		}
		
		// add the user to the body of the response
		ObjectMapper mapper = new ObjectMapper();
		String userJsonString = mapper.writeValueAsString(authResult.getPrincipal());
		response.getWriter().write(userJsonString);
		
	}
	
}
