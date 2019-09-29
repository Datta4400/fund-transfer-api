package com.mycompany.fundtransfer_service.account.repository;


import com.mycompany.fundtransfer_service.transaction.repository.DefaultTransactionRepository;
import com.mycompany.fundtransfer_service.transaction.repository.TransactionRepository;

import lombok.Getter;

@Getter
public class RepositoryFactory {

	private final AccountsRepository accountsRepository;
	private final TransactionRepository transactionRepository;

	private RepositoryFactory(AccountsRepository accountsRepository,
			TransactionRepository transactionRepository) {

		this.accountsRepository = accountsRepository;
		this.transactionRepository = transactionRepository;
	}

	public static RepositoryFactory create() {
		
		final AccountsRepository accountsRepository = new DefaultAccountRepository();
		final TransactionRepository transactionRepository = new DefaultTransactionRepository();
		return new RepositoryFactory(accountsRepository, transactionRepository);
	}

}
