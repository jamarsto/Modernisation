package uk.me.jasonmarston.legacy.controller.authentication.impl;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {
	private static final long serialVersionUID = 1L;
	private final String uid;
	private final String email;
	private final String username;
	private final String issuer;
	private final String picture;
	private final String credentials;
	private final Collection<? extends GrantedAuthority> authorities;

	public User(final String uid,
				final String email,
				final String username,
				final String issuer,
				final String picture,
				final String credentials,
				final Collection<? extends GrantedAuthority> authorities) {
		this.uid = uid;
		this.email = email;
		this.username = username;
		this.issuer = issuer;
		this.picture = picture;
		this.credentials = credentials;
		this.authorities = authorities;
	}

	public String getUid() {
		return uid;
	}

	public String getEmail() {
		return email;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getPicture() {
		return picture;
	}

	public String getCredentials() {
		return credentials;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
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
