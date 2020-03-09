package com.pizzashop.auth;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pizzashop.models.Customer;
import com.pizzashop.models.UserAddress;

import lombok.Data;

@Entity
@Data
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private final String username;
	private final String password;
	
	/**
	 * A user can have multiple granted authorities in the application.
	 */
	@OneToMany(fetch = FetchType.EAGER) // pull child entities automatically
	@Cascade(CascadeType.ALL) // Save all objects in the collection
	private final Set<ApplicationGrantedAuthority> grantedAuthorities;
	
	private String firstName;
	private String lastName;
	private String email;	
	
	/**
	 * A user can have more than one address.
	 */
	@OneToMany(fetch = FetchType.EAGER) // pull child entities automatically
	@Cascade(CascadeType.ALL) // Save all objects in the collection
	private List<UserAddress> addresses;
	
	public User() {
		username = null;
		password = null;
		grantedAuthorities = null;
	}
	
	public User(String username, String password, Set<ApplicationGrantedAuthority> grantedAuthorities,
			String firstName, String lastName, String email, List<UserAddress> addresses) {
		this.username = username;
		this.password = password;
		this.grantedAuthorities = grantedAuthorities;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.addresses = addresses;
	}

	@Override
	public Collection<ApplicationGrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
