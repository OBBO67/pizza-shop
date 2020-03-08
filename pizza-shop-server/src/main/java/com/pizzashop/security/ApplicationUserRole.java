package com.pizzashop.security;

import static com.pizzashop.security.ApplicationUserPermission.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

import lombok.Getter;

public enum ApplicationUserRole {
	
	CUSTOMER(Sets.newHashSet(MENU_READ, MENU_WRITE));
	
	@Getter
	private final Set<ApplicationUserPermission> permissions;
	
	ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
		Set<SimpleGrantedAuthority> authorities = 
				getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());
		
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return authorities;			
	}
}
