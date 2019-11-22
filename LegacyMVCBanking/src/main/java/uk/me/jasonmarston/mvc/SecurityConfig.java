package uk.me.jasonmarston.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/logout")
				.permitAll()
				.and()
			.httpBasic().disable()
			.authorizeRequests()
				.antMatchers(
					"/error",
					"/favicon.ico",
					"/**/*.png",
					"/**/*.gif",
					"/**/*.svg",
					"/**/*.jpg",
					"/**/*.html",
					"/**/*.css",
					"/**/*.js").permitAll()
				.anyRequest().authenticated();
    }
	
	@SuppressWarnings("deprecation")
	@Bean
    @Override
    public UserDetailsService userDetailsService() {
		final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails user = null;

		user = User.withDefaultPasswordEncoder()
				.username("jason@jasonmarston.me.uk")
				.password("password")
				.roles("USER")
				.build();
        manager.createUser(user);

        user = User.withDefaultPasswordEncoder()
        		.username("jason.marston@cloudsolutions.co.uk")
	            .password("password")
	            .roles("USER")
	            .build();
        manager.createUser(user);

        return manager;
    }

	@Bean
	public SpringSecurityDialect springSecurityDialect(){
		return new SpringSecurityDialect();
	}
}