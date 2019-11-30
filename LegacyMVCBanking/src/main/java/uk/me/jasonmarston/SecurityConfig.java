package uk.me.jasonmarston;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import uk.me.jasonmarston.mvc.service.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

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
					"/signup",
					"/confirm",
					"/reset",
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

	@Override
	protected void configure(
			AuthenticationManagerBuilder authenticationManagerBuilder)
					throws Exception {
		authenticationManagerBuilder
			.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SpringSecurityDialect springSecurityDialect(){
		return new SpringSecurityDialect();
	}
	
	@Bean
	public TaskExecutor getAsyncExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(20);
		scheduler.setThreadNamePrefix("scheduler-");
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		return scheduler;
	}
}