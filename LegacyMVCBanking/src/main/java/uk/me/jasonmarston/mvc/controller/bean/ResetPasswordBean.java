package uk.me.jasonmarston.mvc.controller.bean;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords do not match!"
)
public class ResetPasswordBean extends AbstractBean {
	@NotNull(message = "Password is required")
	@NotEmpty(message = "Password is required")
	private String password;
	
	@NotNull(message = "Password confirmation is required")
	@NotEmpty(message = "Password confirmation is required")
	private String passwordConfirmation;

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}