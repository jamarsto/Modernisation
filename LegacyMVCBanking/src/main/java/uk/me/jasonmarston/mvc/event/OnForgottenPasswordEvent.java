package uk.me.jasonmarston.mvc.event;

import java.security.InvalidParameterException;

import org.springframework.context.ApplicationEvent;

import uk.me.jasonmarston.domain.value.EmailAddress;

public class OnForgottenPasswordEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private EmailAddress email;
	private String url;

	public OnForgottenPasswordEvent(
			final EmailAddress email,
			final String url) {
		super(email);
		if(email == null || url == null) {
			throw new InvalidParameterException("email and url are required");
		}
		this.email = email;
		this.url = url;
	}

	public EmailAddress getEmail() {
		return email;
	}

	public String getUrl() {
		return url;
	}
}
