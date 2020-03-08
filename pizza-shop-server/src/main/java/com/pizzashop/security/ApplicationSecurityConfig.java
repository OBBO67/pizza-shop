package com.pizzashop.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pizzashop.auth.ApplicationUserService;
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
			.addFilter(getJwtUsernameAndPasswordAuthenticationFilter())
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
	
	/*
	 * Define a custom implementation of UserDetailsService to load user-specific 
	 * data in the security framework. Also set the encryption method for passwords.
	 */
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserService).passwordEncoder(passwordEncoder);
    }
	
	/*
	 * Sets the URL for the login filter
	 */
	@Bean
	public JwtUsernameAndPasswordAuthenticationFilter getJwtUsernameAndPasswordAuthenticationFilter() 
			throws Exception {
	    final JwtUsernameAndPasswordAuthenticationFilter filter = 
	    		new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey);
	    filter.setFilterProcessesUrl("/api/login");
	    return filter;
	}

}
