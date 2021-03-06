package com.pizzashop.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "application.jwt")
@Component
public class JwtConfig {
	
	private String secretKey;
	private String tokenPrefix;
	private int tokenExpirationAfterDays;
	
	public JwtConfig() {}
	
	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}

}
