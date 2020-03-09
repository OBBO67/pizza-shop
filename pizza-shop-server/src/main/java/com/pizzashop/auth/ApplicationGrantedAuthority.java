package com.pizzashop.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity
@Data
@Table(name = "app_granted_authority")
public final class ApplicationGrantedAuthority implements GrantedAuthority {
	
	private static final long serialVersionUID = -1007655235817181681L;
	
	private @Id @GeneratedValue Long id;
	private final String role;
	
	public ApplicationGrantedAuthority() {
		role = null;
	}

	public ApplicationGrantedAuthority(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role;
	}

}
