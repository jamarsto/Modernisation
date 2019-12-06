package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

public class EmailDetails extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Email is required")
	@Email(message = "Must be a valid email address")
	private String email;
	
	public EmailDetails() {
	}
	
	public EmailDetails(final String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
