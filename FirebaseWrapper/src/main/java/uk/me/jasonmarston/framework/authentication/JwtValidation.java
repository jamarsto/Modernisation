package uk.me.jasonmarston.framework.authentication;

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class JwtValidation {
	private static final Object mutex = new Object();
	private static volatile JwtValidation INSTANCE;
	private final FirebaseAuth auth;
	
	private JwtValidation() throws IOException {
		final FirebaseOptions options = 
				new FirebaseOptions
					.Builder()
						.setCredentials(GoogleCredentials
								.getApplicationDefault())
						.build();
		FirebaseApp.initializeApp(options);
		auth = FirebaseAuth.getInstance();
	}
	
	public static JwtValidation getInstance() throws IOException {
		JwtValidation result = INSTANCE;
		if(result == null) {
			synchronized(mutex) {
				result = INSTANCE;
				if(result == null) {
					INSTANCE = result = new JwtValidation();
				}
			}
		}
		return result;
	}

	public Token verifyIdToken(String token) {
		try {
			return new Token(auth.verifyIdToken(token));
		} catch (FirebaseAuthException e) {
			throw new RuntimeException("Failed to initialize Firebase");
		}
	}
	
}
