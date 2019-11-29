package uk.me.jasonmarston.mvc.controller.command;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

public class SignupForm extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	private String email;

	private String password;
	
	private String passwordConfirmation;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}
