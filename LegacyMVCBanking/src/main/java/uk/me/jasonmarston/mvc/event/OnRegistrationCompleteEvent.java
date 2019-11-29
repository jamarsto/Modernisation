package uk.me.jasonmarston.mvc.event;

import org.springframework.context.ApplicationEvent;

import uk.me.jasonmarston.domain.aggregate.impl.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private User user;
	private String url;

	public OnRegistrationCompleteEvent(User user, String url) {
		super(user);
		this.user = user;
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
