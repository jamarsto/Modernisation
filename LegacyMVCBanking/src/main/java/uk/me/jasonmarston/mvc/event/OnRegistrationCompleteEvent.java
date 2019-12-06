package uk.me.jasonmarston.mvc.event;

import org.springframework.context.ApplicationEvent;

import uk.me.jasonmarston.domain.aggregate.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private User user;
	private String url;

	public OnRegistrationCompleteEvent(final User user, final String url) {
		super(user);
		if(user == null || url == null) {
			throw new RuntimeException("user and url are required");
		}
		this.user = user;
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public String getUrl() {
		return url;
	}
}
