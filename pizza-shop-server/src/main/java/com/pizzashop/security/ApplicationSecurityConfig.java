package com.pizzashop.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pizzashop.auth.ApplicationUserService;
import com.pizzashop.filters.RequestFilter;
import com.pizzashop.jwt.JwtConfig;
import com.pizzashop.jwt.JwtTokenVerifierFilter;
import com.pizzashop.jwt.JwtUsernameAndPasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	public static final String API_ROOT_URL = "/api/**";
	public static final String SIGN_UP_URL = "/api/signup";
	public static final String LOGIN_URL = "/api/login";
	public static final String CHECK_USERNAME_URL = "/api/user/username";
	
	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserService applicationUserService;
	private final SecretKey secretKey;
	private final JwtConfig jwtConfig;
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService,
			SecretKey secretKey, JwtConfig jwtConfig) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
		this.secretKey = secretKey;
		this.jwtConfig = jwtConfig;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers("/css/*", "/js/*").permitAll()
				.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
				.antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
				.antMatchers(HttpMethod.GET, CHECK_USERNAME_URL).permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilterBefore(new RequestFilter(), JwtUsernameAndPasswordAuthenticationFilter.class)
				.addFilter(getJwtUsernameAndPasswordAuthenticationFilter())
				.addFilterAfter(new JwtTokenVerifierFilter(jwtConfig, secretKey),
						JwtUsernameAndPasswordAuthenticationFilter.class);
	}
	
	/**
	 * Define a custom implementation of UserDetailsService to load user-specific 
	 * data in the security framework. Also set the encryption method for passwords.
	 */
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserService).passwordEncoder(passwordEncoder);
    }
	
	/**
	 * Sets the URL for the login filter
	 */
	@Bean
	public JwtUsernameAndPasswordAuthenticationFilter getJwtUsernameAndPasswordAuthenticationFilter() 
			throws Exception {
	    final JwtUsernameAndPasswordAuthenticationFilter filter = 
	    		new JwtUsernameAndPasswordAuthenticationFilter(jwtConfig, secretKey);
	    filter.setAuthenticationManager(authenticationManager());
	    filter.setFilterProcessesUrl("/api/login");
	    return filter;
	}

}
