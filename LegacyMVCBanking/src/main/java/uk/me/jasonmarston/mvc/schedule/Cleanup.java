package uk.me.jasonmarston.mvc.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;

@Component
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.REPEATABLE_READ, 
		readOnly = false)
public class Cleanup {
	@Autowired
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	private UserService userService;
	
	@Scheduled(fixedDelay = 60000)
	public void cleanupVerificationTokens() {
		List<VerificationToken> list = verificationTokenService.findExpiredTokens();
		for(VerificationToken token: list) {
			verificationTokenService.delete(token);
			final User user = userService.getUser(token.getUserId());
			if(!user.isEnabled()) {
				userService.delete(user);
			}
		}
	}
}