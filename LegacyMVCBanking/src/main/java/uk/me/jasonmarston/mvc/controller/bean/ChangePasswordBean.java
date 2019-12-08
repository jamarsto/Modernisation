package uk.me.jasonmarston.mvc.controller.bean;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords do not match!"
)
public class ChangePasswordBean extends AbstractBean {

	@NotNull(message = "Current password is required")
	@NotEmpty(message = "Current password is required")
	private String currentPassword;

	@NotNull(message = "Password is required")
	@NotEmpty(message = "Password is required")
	private String password;
	
	@NotNull(message = "Password confirmation is required")
	@NotEmpty(message = "Password confirmation is required")
	private String passwordConfirmation;
	
	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}