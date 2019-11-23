package uk.me.jasonmarston.rest.controller.impl;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uk.me.jasonmarston.domain.aggregate.impl.Account;
import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.domain.service.AccountService;
import uk.me.jasonmarston.domain.type.impl.Amount;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.rest.controller.AccountController;
import uk.me.jasonmarston.rest.controller.authentication.impl.User;
import uk.me.jasonmarston.rest.controller.message.impl.Message;

@RestController
public class AccountControllerImpl implements AccountController {
    @Autowired
    private AccountService accountService;

    @Override
	@RequestMapping(path = "/accounts/{id}/deposits",
		method=RequestMethod.POST,
		consumes = "application/json", 
		produces = "application/json")
	public ResponseEntity<?> depositFunds(@PathVariable("id") final UUID id, 
			@RequestBody final Amount amount) {
		try {
			Transaction transaction = accountService
					.depositFunds(new EntityId(id), amount);
	    	URI location = ServletUriComponentsBuilder
	    			.fromCurrentRequest()
	    			.path("/{Id}")
	    			.buildAndExpand(transaction.getId().getId())
	    			.toUri();
	        return ResponseEntity.created(location).build();
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
	}

    @Override
    @RequestMapping(path = "/accounts/{id}", 
    	method=RequestMethod.GET,
    	produces = "application/json")
    public ResponseEntity<?> getAccount(@PathVariable("id") final UUID id) {
    	try {
    		final Account account = accountService
    				.getAccount(new EntityId(id));
    		return ResponseEntity.ok(account);
    	}
    	catch(NoSuchElementException e) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Invalid Account"));
    	}
    }

	@Override
	@RequestMapping(path = "/accounts/{id}/balance",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getBalance(@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService.getBalance(new EntityId(id)));
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
	}

	@Override
	@RequestMapping(path = "/accounts/{accountId}/deposits/{id}",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getDeposit(
			@PathVariable("accountId") final UUID accountId,
			@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService.getDeposit(
						new EntityId(accountId), 
						new EntityId(id)));
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account or Deposit"));
		}
	}

	@Override
	@RequestMapping(path = "/accounts/{id}/deposits",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getDeposits(@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService
						.getAccount(new EntityId(id)).getDeposits());
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
	}

	@Override
	@RequestMapping(path = "/accounts/{accountId}/transactions/{id}",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getTransaction(
			@PathVariable("accountId") final UUID accountId,
			@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService.getTransaction(
						new EntityId(accountId), 
						new EntityId(id)));
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account or Transaction"));
		}
	}

	@Override
	@RequestMapping(path = "/accounts/{id}/transactions",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getTransactions(
			@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService
						.getAccount(new EntityId(id)).getTransactions());
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
	}

	@Override
	@RequestMapping(path = "/accounts/{accountId}/withdrawals/{id}",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getWithdrawal(
			@PathVariable("accountId") final UUID accountId,
			@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService.getWithdrawal(
						new EntityId(accountId), 
						new EntityId(id)));
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account or Withdrawal"));
		}
	}

	@Override
	@RequestMapping(path = "/accounts/{id}/withdrawals",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getWithdrawals(
			@PathVariable("id") final UUID id) {
		try {
			return ResponseEntity
				.ok(accountService
						.getAccount(new EntityId(id)).getWithdrawals());
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
	}

	@Override
    //@PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/accounts", 
    	method=RequestMethod.POST,
    	produces = "application/json")
    public ResponseEntity<?> openAccount(
    		@AuthenticationPrincipal final User user) {
    	final Account account = accountService.openAccount();
    	URI location = ServletUriComponentsBuilder
    			.fromCurrentRequest()
    			.path("/{id}")
    			.buildAndExpand(account.getId().getId())
    			.toUri();
        return ResponseEntity.created(location).build();
    }

	@Override
	@RequestMapping(path = "/accounts/{id}/withdrawals",
		method=RequestMethod.POST,
		consumes = "application/json",
		produces = "application/json")
	public ResponseEntity<?> withdrawFunds(@PathVariable("id") final UUID id,
			@RequestBody final Amount amount) {
		try {
			Transaction transaction = accountService
					.withdrawFunds(new EntityId(id), amount);
	    	URI location = ServletUriComponentsBuilder
	    			.fromCurrentRequest()
	    			.path("/{id}")
	    			.buildAndExpand(transaction.getId().getId())
	    			.toUri();
	        return ResponseEntity.created(location).build();
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
	}
}