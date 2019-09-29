package com.mycompany.fundtransfer_service.account.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.fundtransfer_service.account.domain.AbstractAccount;
import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.domain.currecnies.BaseCurrency;
import com.mycompany.fundtransfer_service.account.domain.currecnies.Currency;

public class DefaultAccountRepository implements AccountsRepository {

	private static final Logger logger = LoggerFactory.getLogger(DefaultAccountRepository.class);

	private final AtomicLong counter;
	private final ConcurrentMap<Long, Account> accounts;

	DefaultAccountRepository() {
		this.counter = new AtomicLong(0L);
		this.accounts = new ConcurrentHashMap<>();

	}
	

	@Override
	public Account addBankAccount(String number, BigDecimal balance) {
		return addBankAccount(BaseCurrency.getDefault(),number, balance);
	}

	@Override
	public Account addBankAccount(Currency currency, String number, BigDecimal balance) {
		String currencyIsoCode = currency.getISOCode();
		final Account account = AbstractAccount.addSavingAccount(counter.incrementAndGet(), currencyIsoCode, number, balance);
		accounts.putIfAbsent(account.getId(), account);
		logger.trace(String.format("Bank account added with id {}", account.getId())) ;
		return account;
	}

	@Override
	public Optional<Account> getById(Long id) {			
		return Optional.ofNullable(accounts.get(id));
	}

	@Override
	public int size() {
		return accounts.size();
	}

	@Override
	public BigDecimal getBalance(Long id) {
		return accounts.get(id).getBalance();
	}

	@Override
	public Collection<Account> getAll() {
		final Collection<Account> allAccounts = accounts.values().stream().collect(Collectors.toList());
		return allAccounts;
	}

}
