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

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.mvc.event.OnForgottenPasswordEvent;

@Component
public class ForgottenListener implements
		ApplicationListener<OnForgottenPasswordEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ForgottenListener.class);

	@Autowired
	@Lazy
	private ResetTokenService resetTokenService;
	
	@Autowired
	@Lazy
	private JavaMailSender sender;
	
	@Value("${SPRING_MAIL_FROM}")
    private String from;
	
	@Async
	@Override
	public void onApplicationEvent(final OnForgottenPasswordEvent event) {
		final ResetToken token = resetTokenService
				.create(event.getEmailDetails());
		if(token == null) {
			LOGGER.warn("Email: "
					+ event.getEmailDetails().getEmail() 
					+ " does not match with a user.");
			return;
		}
		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(event.getEmailDetails().getEmail());
			helper.setFrom(from);
			helper.setSubject("Password Reset Confirmation");
			helper.setText("<a href=\"http://localhost:8080" 
					+ event.getUrl() 
					+ "/user/forgotten/verification?token=" 
					+ token.getToken()
					+ "\">Confirm Password Reset</a>", true);
			sender.send(message);
		} catch (RuntimeException e ) {
			LOGGER.error("Failed to send confirmation email: " + e.getMessage());
		} catch (MessagingException e) {
			LOGGER.error("Failed to send confirmation email: " + e.getMessage());
		}
	}
}
