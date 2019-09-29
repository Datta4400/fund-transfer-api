package com.mycompany.fundtransfer_service.account.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.mycompany.fundtransfer_service.account.domain.Account;

public class AccountRepositoryTest {

	final AccountsRepository repository = RepositoryFactory.create().getAccountsRepository();

	@Test
	public void given_valid_details_should_add_account() {
		repository.addBankAccount("10001", BigDecimal.valueOf(5000));
		Account account = repository.getById(1L).get();
		assertNotNull(account);
		assertEquals(account.getBalance(), BigDecimal.valueOf(5000));
		assertEquals(account.getNumber(), "10001");
		assertEquals(account.getCurrency(), "INR");

	}

	@Test
	public void should_return_all_accounts() {
		repository.addBankAccount("10001", BigDecimal.valueOf(6000));
		repository.addBankAccount("10001", BigDecimal.valueOf(5000));
		assertEquals(2, repository.size());

	}

	@Test
	public void should_return_balance() {
		repository.addBankAccount("10001", BigDecimal.valueOf(6000));		
		assertEquals(BigDecimal.valueOf(6000), repository.getBalance(1L));

	}
	
	@Test
	public void should_return_account_by_id() {
		repository.addBankAccount("10001", BigDecimal.valueOf(6000));
		Account account = repository.getById(1L).get();
		assertTrue(account.getId() == 1L);
	}

}
