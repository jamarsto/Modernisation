package uk.me.jasonmarston.domain.aggregate.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Entity
@Table(name = "ACCOUNTS")
public class User extends AbstractAggregate implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String uid;
	private String email;
	private String username;

	@Transient
	private String issuer;

	private String picture;
	
	@Transient
	private String credentials;

	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Collection<GrantedAuthority> authorities = 
			new ArrayList<GrantedAuthority>();
	
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	public User() {
		this.setId(new EntityId());
	}

	public User(final String uid,
				final String email,
				final String username,
				final String picture) {
		this.setId(new EntityId());
		this.uid = uid;
		this.email = email;
		this.username = username;
		this.picture = picture;
		this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		this.password = null;
		this.issuer = null;
		this.credentials = null;
	}
	
	public User(final String email, final String password) {
		this.setId(new EntityId());
		this.uid = UUID.randomUUID().toString();
		this.email = email;
		this.username = email;
		this.picture = null;
		this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		this.password = password;
		this.issuer = null;
		this.credentials = null;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getCredentials() {
		return credentials;
	}

	public String getEmail() {
		return email;
	}

	public String getIssuer() {
		return issuer;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public String getPicture() {
		return picture;
	}

	public String getUid() {
		return uid;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setAccountNonExpired(final boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(final boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setAuthorities(
			final Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setCredentials(final String credentials) {
		this.credentials = credentials;
	}

	public void setCredentialsNonExpired(
			final boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setIssuer(final String issuer) {
		this.issuer = issuer;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setUid(final String uid) {
		this.uid = uid;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}