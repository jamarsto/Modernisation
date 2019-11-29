package uk.me.jasonmarston.domain.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.repository.VerificationTokenRepository;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Transactional(propagation = Propagation.REQUIRED,
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class VerificationTokenServiceImpl implements VerificationTokenService {
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public VerificationToken findByToken(String token) {
		final Optional<VerificationToken> optional = verificationTokenRepository.findByToken(token);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public VerificationToken create(EntityId id) {
		final VerificationToken token = new VerificationToken(id);
		return verificationTokenRepository.save(token);
	}
}