package uk.me.jasonmarston.mvc.event;

import org.springframework.context.ApplicationEvent;

import uk.me.jasonmarston.domain.details.EmailDetails;

public class OnForgottenPasswordEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private EmailDetails emailDetails;
	private String url;

	public OnForgottenPasswordEvent(
			final EmailDetails emailDetails,
			final String url) {
		super(emailDetails);
		if(emailDetails == null || url == null) {
			throw new RuntimeException("email and url are required");
		}
		this.emailDetails = emailDetails;
		this.url = url;
	}

	public EmailDetails getEmailDetails() {
		return emailDetails;
	}

	public String getUrl() {
		return url;
	}
}
