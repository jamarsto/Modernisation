package uk.me.jasonmarston.mvc.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.mvc.event.OnRegistrationCompleteEvent;

@Component
public class RegistrationListener implements
		ApplicationListener<OnRegistrationCompleteEvent> {
	@Autowired
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	private JavaMailSender sender;
	
	@Override
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		final User user = event.getUser();
		final VerificationToken token = verificationTokenService
				.create(user.getId());
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setFrom("noreply@jasonmarston.me.uk");
		message.setSubject("Registration Confirmation");
		message.setText("http://localhost:8080" 
				+ event.getUrl() 
				+ "/confirm?token=" 
				+ token.getToken());
		sender.send(message);
	}
}
