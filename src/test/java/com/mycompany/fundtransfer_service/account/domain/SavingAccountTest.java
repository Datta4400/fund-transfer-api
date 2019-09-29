package com.mycompany.fundtransfer_service.account.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.mycompany.fundtransfer_service.account.domain.currecnies.BaseCurrency;
import com.mycompany.fundtransfer_service.account.repository.RepositoryFactory;
import com.mycompany.fundtransfer_service.common.Repository;
import com.mycompany.fundtransfer_service.common.utils.JsonUtils;

public class SavingAccountTest {

	final Repository<Account> accountRepository = RepositoryFactory.create().getAccountsRepository();

	@Test
	void given_valid_account_should_return_account_id() {
		Account account = addAccount();
		assertNotNull(account);
		assertEquals(Long.valueOf(1l), account.getId());
	}

	@Test
	void given_valid_account_should_return_account_number() {
		Account account = addAccount();
		assertNotNull(account);
		assertEquals("10001", account.getNumber());
	}

	@Test
	void given_valid_account_should_return_account_balance() {
		Account account = addAccount();
		assertNotNull(account);
		assertEquals(BigDecimal.valueOf(5000), account.getBalance());
	}

	@Test
	void given_valid_details_should_add_account() {
		Account account = addAccount();
		assertNotNull(account);
	}

	@Test
	void given_null_values_should_throw_null_pointer_exception() {
		NullPointerException exception = assertThrows(NullPointerException.class,
				() -> SavingAccount.addSavingAccount(null, null, null, null));
		assertEquals("Id cannot be null", exception.getLocalizedMessage());

		exception = assertThrows(NullPointerException.class,
				() -> SavingAccount.addSavingAccount(1L, null, null, null));
		assertEquals("Currency cannot be null", exception.getLocalizedMessage());

		exception = assertThrows(NullPointerException.class,
				() -> SavingAccount.addSavingAccount(1L, BaseCurrency.getDefaultAsString(), null, null));
		assertEquals("Number cannot be null", exception.getLocalizedMessage());

		exception = assertThrows(NullPointerException.class,
				() -> SavingAccount.addSavingAccount(1L, BaseCurrency.getDefaultAsString(), "1001", null));
		assertEquals("Balance cannot be null", exception.getLocalizedMessage());

	}

	@Test
	void given_invalid_values_should_throw_illegal_argument_exception() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> SavingAccount
				.addSavingAccount(1L, BaseCurrency.getDefaultAsString(), "10001", BigDecimal.valueOf(-100)));
		assertEquals("Amount must be a positive value", exception.getLocalizedMessage());

		exception = assertThrows(IllegalArgumentException.class, () -> SavingAccount.addSavingAccount(1L,
				BaseCurrency.getDefaultAsString(), "1**1", BigDecimal.valueOf(10000)));
		assertEquals("Please enter valid account number", exception.getLocalizedMessage());

	}

	@Test
	void given_valid_amount_should_perform_credit_debit_transaction() {
		final Account account = addAccount();
		assertTrue(account.debit(BigDecimal.valueOf(1000)));
		assertTrue(account.credit(BigDecimal.valueOf(1000)));
		assertEquals(BigDecimal.valueOf(5000), account.getBalance());
		assertTrue(account.debit(BigDecimal.valueOf(1000)));
		assertEquals(BigDecimal.valueOf(4000), account.getBalance());
	}

	@Test
	void lockShouldBeTransient() {
		final Account account = addAccount();
		final String json = JsonUtils.build().toJson(account);
		assertNotNull(json);
		assertFalse(json.contains("lock"));
	}

	private Account addAccount() {
		Account accunt = SavingAccount.addSavingAccount(1L, BaseCurrency.getDefaultAsString(), "10001",
				new BigDecimal(5000));
		return accunt;
	}

}
