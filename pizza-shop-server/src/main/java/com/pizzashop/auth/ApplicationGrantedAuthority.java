package com.pizzashop.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity
@Data
public final class ApplicationGrantedAuthority implements GrantedAuthority {
	
	private static final long serialVersionUID = -1007655235817181681L;
	
	private @Id @GeneratedValue Long id;
	private final String role;

	public ApplicationGrantedAuthority(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role;
	}

}
