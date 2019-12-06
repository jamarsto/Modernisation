package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

public class TokenDetails extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Token is required")
	private String token;
	
	public TokenDetails() {
	}
	
	public TokenDetails(final String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
