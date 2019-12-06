package uk.me.jasonmarston.domain.service.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.details.EmailDetails;
import uk.me.jasonmarston.domain.details.TokenDetails;
import uk.me.jasonmarston.domain.factory.aggregate.ResetTokenBuilderFactory;
import uk.me.jasonmarston.domain.repository.ResetTokenRepository;
import uk.me.jasonmarston.domain.repository.UserRepository;
import uk.me.jasonmarston.domain.repository.specification.ResetTokenSpecification;
import uk.me.jasonmarston.domain.service.ResetTokenService;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED,
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class ResetTokenServiceImpl implements ResetTokenService {

	@Autowired
	@Lazy
	private ResetTokenBuilderFactory resetTokenBuilderFactory;
	
	@Autowired
	@Lazy
	private ResetTokenRepository resetTokenRepository;
	
	@Autowired
	@Lazy
	private UserRepository userRepository;

	@Override
	public ResetToken create(@NotNull @Valid final EmailDetails emailDetails) {
		final Optional<User> optional = userRepository
				.findByEmail(emailDetails.getEmail());
		if(optional.isPresent()) {
			final User user = optional.get();

			final ResetToken.Builder builder = resetTokenBuilderFactory
					.create();

			final ResetToken token = builder
					.forUserId(user.getId())
					.build();

			return resetTokenRepository.save(token);
		}
		return null;
	}

	@Override
	public void delete(@NotNull @Valid final ResetToken resetToken) {
		resetTokenRepository.delete(resetToken);
	}

	@Override
	public ResetToken findByToken(
			@NotNull @Valid final TokenDetails tokenDetails) {
		final Optional<ResetToken> optional = resetTokenRepository
				.findByToken(tokenDetails.getToken());
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<ResetToken> findExpiredTokens() {
		return resetTokenRepository
				.findAll(
						new ResetTokenSpecification
								.ResetTokenIsExpired());
	}
}