package uk.me.jasonmarston.legacy.controller.authentication.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import uk.me.jasonmarston.framework.authentication.JwtValidation;
import uk.me.jasonmarston.framework.authentication.Token;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
	JwtValidation auth = null;

	public TokenAuthenticationFilter() {
		try {
			auth = JwtValidation.getInstance();
		} catch (final IOException e) {
			logError(e);
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
				final Token token = auth.verifyIdToken(jwt);
				final List<GrantedAuthority> authorities = Collections
						.singletonList(
								new SimpleGrantedAuthority("ROLE_USER"));
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
							new WebAuthenticationDetailsSource()
								.buildDetails(request)
					);

				SecurityContextHolder.getContext()
						.setAuthentication(authentication);
				try {
					filterChain.doFilter(request, response);
				}
				catch(final NestedServletException e) { 
					logError(e);
					response.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Internal server error");
				}
			}
			else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Please provide credentials");
			}
        }
		// Catches the runtime exception from our token authenticator
		// either as it is null from failed configuration, 
		// or if we get an invalid token,
		// and anything else missed by the chain
		catch(final RuntimeException | Error e) {
			logError(e);
			response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Internal server error");
		}
		catch (final Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Please provide valid credentials");
        }
	}

	private String getJwtFromRequest(final HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) 
				&& bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
    }
	
	private void logError(final Throwable e) {
		final StringWriter stack = new StringWriter();
	    e.printStackTrace(new PrintWriter(stack));
		LOGGER.error(stack.toString());
	}
}
