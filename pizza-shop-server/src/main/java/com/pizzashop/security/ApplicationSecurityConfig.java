package com.pizzashop.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pizzashop.jwt.JwtConfig;
import com.pizzashop.jwt.JwtTokenVerifierFilter;
import com.pizzashop.jwt.JwtUsernameAndPasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserService appUserService;
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService appUserService,
			JwtConfig jwtConfig, SecretKey secretKey) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.appUserService = appUserService;
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
					authenticationManager(), jwtConfig, secretKey))
			.addFilterAfter(new JwtTokenVerifierFilter(jwtConfig, secretKey),
					JwtUsernameAndPasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers("/", "/api/login", "/api/signup", "/css/*", "/js/*")
				.permitAll()
			.antMatchers("/menu/**")
				.hasRole(ApplicationUserRole.CUSTOMER.name())
			.anyRequest()
			.authenticated();
	}

}
