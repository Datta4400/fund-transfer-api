package com.mycompany.fundtransfer_service.transaction.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.repository.AccountsRepository;
import com.mycompany.fundtransfer_service.account.repository.RepositoryFactory;
import com.mycompany.fundtransfer_service.transaction.repository.TransactionRepository;

public class MoneyTransactionTest {

	private AccountsRepository accountRepository;

	private Account creditAccount;
	private Account debitAccount;
	private TransactionRepository transactionRepository;
	private Transaction transaction;

	@BeforeEach
	public void init() {
		accountRepository = RepositoryFactory.create().getAccountsRepository();
		creditAccount = accountRepository.addBankAccount("10001", BigDecimal.valueOf(5000));
		debitAccount = accountRepository.addBankAccount("10002", BigDecimal.valueOf(6000));
		transactionRepository = RepositoryFactory.create().getTransactionRepository();
	}

	@Test
	public void given_null_inputs_should_throw_exception() {

		NullPointerException exception = assertThrows(NullPointerException.class,
				() -> MoneyTransaction.execute(null, null, null, null));
		assertEquals("Id cannot be null", exception.getLocalizedMessage());

		exception = assertThrows(NullPointerException.class, () -> MoneyTransaction.execute(1L, null, null, null));
		assertEquals("Debit account cannot be null", exception.getLocalizedMessage());

		exception = assertThrows(NullPointerException.class, () -> MoneyTransaction.execute(1L,
				accountRepository.getById(1L).get(), accountRepository.getById(2L).get(), null));
		assertEquals("Amount cannot be null", exception.getLocalizedMessage());

	}

	@Test
	public void given_valid_inputs_should_add_transaction() {
		transaction = transactionRepository.add(debitAccount, creditAccount, BigDecimal.valueOf(5000));
		assertNotNull(transaction);
		assertEquals("10001", transaction.getCredit().getNumber());
		assertEquals("10002", transaction.getDebit().getNumber());
		assertEquals(BigDecimal.valueOf(5000), transaction.getAmount());

	}

	@Test
	public void given_valid_transaction_should_retrurn_id() {
		transaction = transactionRepository.add(creditAccount, debitAccount, BigDecimal.valueOf(5000));
		assert (0 != transaction.getId());
	}

}
