package com.pizzashop.security;

import static com.pizzashop.security.ApplicationUserPermission.*;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.pizzashop.auth.ApplicationGrantedAuthority;

import lombok.Getter;

public enum ApplicationUserRole {
	
	CUSTOMER(Sets.newHashSet(MENU_READ)),
	ADMIN(Sets.newHashSet(MENU_READ, MENU_WRITE));
	
	@Getter
	private final Set<ApplicationUserPermission> permissions;
	
	ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<ApplicationGrantedAuthority> getGrantedAuthorities() {
		Set<ApplicationGrantedAuthority> authorities = 
				getPermissions()
				.stream()
				.map(permission -> new ApplicationGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());
		
		authorities.add(new ApplicationGrantedAuthority("ROLE_" + this.name()));
		
		return authorities;
	}
}
