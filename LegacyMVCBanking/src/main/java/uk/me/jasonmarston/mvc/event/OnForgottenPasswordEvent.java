package uk.me.jasonmarston.mvc.event;

import org.springframework.context.ApplicationEvent;

public class OnForgottenPasswordEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String url;

	public OnForgottenPasswordEvent(String email, String url) {
		super(email);
		this.email = email;
		this.url = url;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
