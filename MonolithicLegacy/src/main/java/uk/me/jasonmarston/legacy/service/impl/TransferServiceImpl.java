package uk.me.jasonmarston.legacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.legacy.domain.type.impl.Amount;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.service.AccountService;
import uk.me.jasonmarston.legacy.service.TransferService;

@Service
@Transactional(propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ,
		readOnly = false)
public class TransferServiceImpl implements TransferService {

	@Autowired
	private AccountService accountService;

	@Override
	public void transferFunds(EntityId fromAccount,
			EntityId toAccount,
			Amount amount) {
		accountService.withdrawFunds(fromAccount, amount);
		accountService.depositFunds(toAccount, amount);
	}

}
