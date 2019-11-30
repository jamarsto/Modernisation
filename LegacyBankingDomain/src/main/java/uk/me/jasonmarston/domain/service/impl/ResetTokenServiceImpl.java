package uk.me.jasonmarston.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.repository.ResetTokenRepository;
import uk.me.jasonmarston.domain.repository.UserRepository;
import uk.me.jasonmarston.domain.repository.specification.impl.ResetTokenSpecification;
import uk.me.jasonmarston.domain.service.ResetTokenService;

@Service
@Transactional(propagation = Propagation.REQUIRED,
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class ResetTokenServiceImpl implements ResetTokenService {
	@Autowired
	private ResetTokenRepository resetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public ResetToken findByToken(String token) {
		final Optional<ResetToken> optional = resetTokenRepository.findByToken(token);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public ResetToken create(String email) {
		final Optional<User> optional = userRepository.findByEmail(email);
		if(optional.isPresent()) {
			final User user = optional.get();
			final ResetToken token = new ResetToken(user.getId());
			return resetTokenRepository.save(token);
		}
		return null;
	}

	@Override
	public void delete(ResetToken resetToken) {
		resetTokenRepository.delete(resetToken);
	}

	@Override
	public List<ResetToken> findExpiredTokens() {
		return resetTokenRepository
				.findAll(
						new ResetTokenSpecification
								.ResetTokenIsExpired());
	}
}