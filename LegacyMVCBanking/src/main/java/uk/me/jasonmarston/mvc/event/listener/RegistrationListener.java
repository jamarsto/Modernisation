package uk.me.jasonmarston.mvc.event.listener;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.aggregate.VerificationToken;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.mvc.event.OnRegistrationCompleteEvent;

@Component
public class RegistrationListener implements
		ApplicationListener<OnRegistrationCompleteEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationListener.class);

	@Autowired
	@Lazy
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	@Lazy
	private JavaMailSender sender;
	
	@Value("${SPRING_MAIL_FROM}")
    private String from;
	
	@Async
	@Override
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		final User user = event.getUser();
		final VerificationToken token = verificationTokenService
				.create(user.getId());
		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(user.getEmail());
			helper.setFrom(from);
			helper.setSubject("Registration Confirmation");
			helper.setText("<a href=\"http://localhost:8080" 
					+ event.getUrl() 
					+ "/user/registration/verification?token=" 
					+ token.getToken()
					+ "\">Confirm Email Address</a>", true);
			sender.send(message);
		} catch (RuntimeException e ) {
			LOGGER.error("Failed to send confirmation email: " + e.getMessage());
		} catch (MessagingException e) {
			LOGGER.error("Failed to send confirmation email: " + e.getMessage());
		}
	}
}