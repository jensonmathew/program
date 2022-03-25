/**
 * 
 */
package com.tmobile.hello.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tmobile.hello.repository.model.Featureflags;

/**
 * @author Sony K Sunny
 *
 */
public class SecurityUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private int userId;
	private boolean isActive;
	private List<GrantedAuthority> authorities;
	
	public SecurityUserDetails(Featureflags featureflags) {
		this.userName = featureflags.getUser();
		this.password = new BCryptPasswordEncoder().encode(featureflags.getPassword());
		this.isActive = featureflags.getIsActive();
		this.userId = featureflags.getId();
		this.authorities = Arrays.stream(featureflags.getRoles().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public int getUserId() {
		return this.userId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userName;
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
		return this.isActive;
	}
	
}