package uk.me.jasonmarston.mvc.controller.bean;

import uk.me.jasonmarston.domain.details.EmailDetails;

public class ForgottenBean extends EmailDetails {
	private static final long serialVersionUID = 1L;

	public ForgottenBean() {
		super();
	}

	public ForgottenBean(String email) {
		super(email);
	}
}
