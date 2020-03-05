package com.pizzashop.security;

import lombok.Getter;

public enum ApplicationUserPermission {
	
	MENU_READ("menu:read"),
	MENU_WRITE("menu:write");
	
	@Getter
	private final String permission;
	
	ApplicationUserPermission(String permission) {
		this.permission = permission;
	}

}
