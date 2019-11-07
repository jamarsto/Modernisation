package uk.me.jasonmarston.legacy.authentication.firebase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import uk.me.jasonmarston.legacy.authentication.User;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private FirebaseAuth auth = null;

	public TokenAuthenticationFilter() {
		try {
			final FirebaseOptions options = 
					new FirebaseOptions
						.Builder()
							.setCredentials(GoogleCredentials.getApplicationDefault())
							.build();
			FirebaseApp.initializeApp(options);
			auth = FirebaseAuth.getInstance();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, 
									final HttpServletResponse response, 
									final FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt)) {
				final FirebaseToken token = auth.verifyIdToken(jwt);
				final List<GrantedAuthority> authorities = Collections
						.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
				final UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								new User(token.getUid(),
										token.getEmail(),
										token.getName(),
										token.getIssuer(),
										token.getPicture(),
									 	jwt,
										authorities), 
								jwt, 
								authorities);
				authentication
					.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request)
					);

				SecurityContextHolder.getContext().setAuthentication(authentication);
				try {
					filterChain.doFilter(request, response);
				}
				catch(final NestedServletException e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
							"Internal server error");
				}
			}
			else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
					"Please provide credentials");
			}
        }
		catch (final Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
					"Please provide valid credentials");
        }
	}

	private String getJwtFromRequest(final HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
    }
}
