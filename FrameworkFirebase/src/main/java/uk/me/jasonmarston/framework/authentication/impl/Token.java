package uk.me.jasonmarston.framework.authentication.impl;

import java.util.Map;

import com.google.firebase.auth.FirebaseToken;

public class Token {
	private FirebaseToken token;
	
	Token(FirebaseToken token) {
		this.token = token;
	}

	public String getUid() {
		return token.getUid();
	}

	public int hashCode() {
		return token.hashCode();
	}

	public String getIssuer() {
		return token.getIssuer();
	}

	public String getName() {
		return token.getName();
	}

	public String getPicture() {
		return token.getPicture();
	}

	public String getEmail() {
		return token.getEmail();
	}

	public boolean isEmailVerified() {
		return token.isEmailVerified();
	}

	public Map<String, Object> getClaims() {
		return token.getClaims();
	}

	public boolean equals(Object obj) {
		return token.equals(obj);
	}

	public String toString() {
		return token.toString();
	}
}
