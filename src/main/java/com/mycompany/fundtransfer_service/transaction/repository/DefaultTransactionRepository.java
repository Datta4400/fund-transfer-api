package com.mycompany.fundtransfer_service.transaction.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.transaction.domain.MoneyTransaction;
import com.mycompany.fundtransfer_service.transaction.domain.Transaction;



public class DefaultTransactionRepository implements TransactionRepository {

	private final AtomicLong counter = new AtomicLong(0L);
	private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

	@Override
	public Optional<Transaction> getById(Long id) {
		return Optional.ofNullable(transactions.get(id));
	}

	@Override
	public int size() {
		return transactions.size();
	}

	@Override
	public Transaction add(Account debit, Account credit, BigDecimal amount) {
		final Transaction transaction = MoneyTransaction.execute(counter.incrementAndGet(), debit, credit, amount);
		transactions.putIfAbsent(transaction.getId(), transaction);
		return transaction;
	}

	@Override
	public Collection<Transaction> getByAccount(Account account) {
		Objects.requireNonNull(account, "Account cannot be null");
		Predicate<Transaction> predicate = t -> t.getDebit().equals(account) || t.getCredit().equals(account);

		Collection<Transaction> filteredTransactions = transactions.values().stream().filter(predicate)
				.sorted(Comparator.comparing(Transaction::getId)).collect(Collectors.toCollection(LinkedList::new));

		return filteredTransactions;

	}

	
	@Override
	public Collection<Transaction> getAll() {	
		final Collection<Transaction> allTransactions = transactions.values().stream().collect(Collectors.toList());
		return allTransactions;
	}

}
