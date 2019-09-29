package com.mycompany.fundtransfer_service.common.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.dto.TransactionDTO;
import com.mycompany.fundtransfer_service.account.repository.AccountsRepository;
import com.mycompany.fundtransfer_service.account.repository.RepositoryFactory;
import com.mycompany.fundtransfer_service.transaction.domain.Transaction;

public class TransferServiceTest {

	private TransactionDTO payload;
	private AccountsRepository accountRepository;

	private Account creditAccount;
	private Account debitAccount;

	@BeforeEach
	public void init() {
		payload = TransactionDTO.builder().creditAccountId(1L).debitAccountId(2L).amount(BigDecimal.valueOf(500))
				.build();
		accountRepository = RepositoryFactory.create().getAccountsRepository();
		creditAccount = accountRepository.addBankAccount("10001", BigDecimal.valueOf(5000));
		debitAccount = accountRepository.addBankAccount("10002", BigDecimal.valueOf(6000));

	}

	@Test
	public void giev_valid_input_should_transfer_money() {

		final Transaction transaction = RepositoryFactory.create().getTransactionRepository().add(creditAccount,
				debitAccount, payload.getAmount());
		boolean status = transaction.run();

		assertTrue(status);

	}
	
	

}