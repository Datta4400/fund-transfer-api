package com.mycompany.fundtransfer_service.common.utils;

import java.util.Objects;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.dto.TransactionDTO;
import com.mycompany.fundtransfer_service.account.exceptions.NoDataFound;
import com.mycompany.fundtransfer_service.account.repository.RepositoryFactory;
import com.mycompany.fundtransfer_service.common.Repository;
import com.mycompany.fundtransfer_service.transaction.domain.Transaction;

import lombok.Getter;

public class TransferService {
	@Getter
	private final RepositoryFactory factory;

	private TransferService(RepositoryFactory factory) {
		this.factory = factory;
	}

	public void generateData() {
		DataGenerator.getInstance(factory).generate();
	}

	public Transaction transfer(TransactionDTO payload) {
		Objects.requireNonNull(payload, "Transaction data cannot be null");
		final Repository<Account> accountRepository = factory.getAccountsRepository();
		
		final Account debit = accountRepository.getById(payload.getDebitAccountId())
				.orElseThrow(() -> new NoDataFound(payload.getDebitAccountId(), "Debit Account doesnt exist"));
		final Account credit = accountRepository.getById(payload.getCreditAccountId())
				.orElseThrow(() -> new NoDataFound(payload.getDebitAccountId(), "Credit Account doesnt exist"));
		
		final Transaction transaction = factory.getTransactionRepository().add(debit, credit, payload.getAmount());
		transaction.run();
		return transaction;
	}
	
	private static class LazyHolder {
		static final TransferService INSTANCE = new TransferService(RepositoryFactory.create());
	}

	public static TransferService getInstance() {
		return LazyHolder.INSTANCE;
	}
}
