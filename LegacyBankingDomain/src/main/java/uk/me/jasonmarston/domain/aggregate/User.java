package uk.me.jasonmarston.domain.aggregate;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.aggregate.UserBuilderFactory;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Configurable(
		preConstruction = true,
		autowire = Autowire.BY_TYPE,
		dependencyCheck = false)
@Entity
@Table(name = "USERS")
public class User extends AbstractAggregate implements UserDetails {
	public static class Builder implements IBuilder<User> {
		private EmailAddress email;
		private Password password;
		private EntityId id;
		private Locale locale;
		private Set<GrantedAuthority> authorities = 
				new HashSet<GrantedAuthority>();

		private Builder() {
			this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		
		public Builder addAuthoriy(final SimpleGrantedAuthority authority) {
			authorities.add(authority);
			return this;
		}

		public Builder andId(EntityId id) {
			this.id = id;
			return this;
		}

		@Override
		public User build() {
			if(email == null || password == null || locale == null) {
				throw new 
					InvalidParameterException("Invalid registration details");
			}

			final User user = new User();
			user.email = email;
			user.changePassword(password);
			user.locale = locale;
			if(id != null) {
				user.setId(id);
			}
			user.authorities = authorities;

			return user;
		}

		public Builder forEmail(final EmailAddress email) {
			this.email = email;
			return this;
		}

		public Builder inLocale(final Locale locale) {
			this.locale = locale;
			return this;
		}

		public Builder withPassword(final Password password) {
			this.password = password;
			return this;
		}
	}

	@Service
	public static class Factory implements UserBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@Autowired
	@Lazy
	@Transient
	private PasswordEncoder passwordEncoder;

	@NotNull
	@Column(unique = true)
	private EmailAddress email;

	private Locale locale;

	@Transient
	private String issuer = null;

	private String picture = null;

	@Transient
	private String credentials = null;

	private Password password;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<GrantedAuthority> authorities = 
			new HashSet<GrantedAuthority>();

	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = false;

	private User() {
		super();
	}

	public boolean addAuthority(final GrantedAuthority authority) {
		return authorities.add(authority);
	}

	public void changePassword(final Password password) {
		this.password = new Password(passwordEncoder
				.encode(password.toString()));
	}

	public void enable() {
		enabled = true;
	}

	@Override
	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getCredentials() {
		return credentials;
	}

	public String getEmail() {
		return email.toString();
	}

	public String getIssuer() {
		return issuer;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String getPassword() {
		return password.toString();
	}

	public String getPicture() {
		return picture;
	}

	@Override
	public String getUsername() {
		return email.toString();
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

	public boolean isCurrentPassword(final Password password) {
		return passwordEncoder.matches(
				password.getPassword(),
				this.password.getPassword());
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public boolean removeAuthority(final GrantedAuthority authority) {
		return authorities.remove(authority);
	}
}