package uk.me.jasonmarston.legacy.controler;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import uk.me.jasonmarston.legacy.authentication.User;
import uk.me.jasonmarston.legacy.domain.type.impl.Amount;

public interface AccountController {
	ResponseEntity<?> depositFunds(final UUID id, final Amount amount);
	ResponseEntity<?> getAccount(final UUID id);
	ResponseEntity<?> getBalance(final UUID id);
	ResponseEntity<?> getDeposit(final UUID accountId, final UUID id);
	ResponseEntity<?> getDeposits(final UUID id);
	ResponseEntity<?> getTransaction(final UUID accountId, final UUID id);
	ResponseEntity<?> getTransactions(final UUID id);
	ResponseEntity<?> getWithdrawal(final UUID accountId, final UUID id);
	ResponseEntity<?> getWithdrawals(final UUID id);
	ResponseEntity<?> openAccount(final User user);
	ResponseEntity<?> withdrawFunds(final UUID id, final Amount amount);
}
