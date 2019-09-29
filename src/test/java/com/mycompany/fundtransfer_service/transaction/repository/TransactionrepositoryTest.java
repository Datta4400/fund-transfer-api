package com.mycompany.fundtransfer_service.transaction.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.repository.AccountsRepository;
import com.mycompany.fundtransfer_service.account.repository.RepositoryFactory;
import com.mycompany.fundtransfer_service.transaction.domain.Transaction;

public class TransactionrepositoryTest {

	final TransactionRepository transactionRepository = RepositoryFactory.create().getTransactionRepository();
	final AccountsRepository accountRepository = RepositoryFactory.create().getAccountsRepository();

	@Test
	public void given_valid_input_should_add_transaction() {

		final Account debitAccount = accountRepository.addBankAccount("10001", BigDecimal.valueOf(5000));
		final Account creditAccount = accountRepository.addBankAccount("10001", BigDecimal.valueOf(6000));

		Transaction trn = transactionRepository.add(debitAccount, creditAccount, BigDecimal.valueOf(3000));

		assertNotNull(trn);

	}

	@Test
	public void given_valid_id_should_return_transaction() {
		final Account debitAccount = accountRepository.addBankAccount("10001", BigDecimal.valueOf(5000));
		final Account creditAccount = accountRepository.addBankAccount("10001", BigDecimal.valueOf(6000));

	    transactionRepository.add(debitAccount, creditAccount, BigDecimal.valueOf(3000));
		Collection<Transaction> trns = transactionRepository.getByAccount(creditAccount);
		assertEquals(1, trns.size());

	}

}
