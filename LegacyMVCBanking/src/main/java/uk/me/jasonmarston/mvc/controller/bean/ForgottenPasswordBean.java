package uk.me.jasonmarston.mvc.controller.bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ForgottenPasswordBean extends AbstractBean {
	@NotNull(message = "Email is required")
	@Email(message = "Must be a valid email address")
	private String email;
	
	public ForgottenPasswordBean() {
	}
	
	public ForgottenPasswordBean(final String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
