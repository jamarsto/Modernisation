package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords must match"
)
public class RegistrationDetails extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Email is required")
	@NotEmpty(message = "Email is required")
	@Email(message = "Must be a valid email address")
	private String email;

	@NotNull(message = "Password is required")
	@NotEmpty(message = "Password is required")
	private String password;
	
	@NotNull(message = "Password confirmation is required")
	@NotEmpty(message = "Password confirmation is required")
	private String passwordConfirmation;
	
	public RegistrationDetails() {
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}
