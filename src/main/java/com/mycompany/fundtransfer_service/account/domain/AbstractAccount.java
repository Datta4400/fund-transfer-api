package com.mycompany.fundtransfer_service.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.fundtransfer_service.common.utils.Validator;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class AbstractAccount implements Account {
	private static final Logger logger = LoggerFactory.getLogger(AbstractAccount.class);

	private final Long id;
	private final String currency;
	private final String number;
	private BigDecimal balance;
	private final transient Lock lock;

	AbstractAccount(Long id, String currency, String number, BigDecimal balance) {
		Objects.requireNonNull(id, "Id cannot be null");
		Objects.requireNonNull(currency, "Currency cannot be null");
		Objects.requireNonNull(number, "Number cannot be null");
		Objects.requireNonNull(balance, "Balance cannot be null");
		Validator.amountNotNegativeOrZero(balance);
		Validator.validateNumber(number);

		this.id = id;
		this.currency = currency;
		this.number = number;
		this.balance = balance;
		this.lock = new ReentrantLock();
	}

	@Override
	public final Long getId() {
		return id;
	}

	@Override
	public final String getNumber() {
		return number;
	}

	@Override
	public final String getCurrency() {
		return currency;
	}

	@Override
	public final BigDecimal getBalance() {
		try {
			lock.lock();
			return balance;
		} finally {
			lock.unlock();
		}
	}

	public static SavingAccount addSavingAccount(Long id, String currency, String number, BigDecimal balance) {
		return new SavingAccount(id, currency, number, balance);
	}

	@Override
	public boolean debit(BigDecimal amount) {
		Objects.requireNonNull(amount, "Amount cannot be null");
		Validator.amountNotNegativeOrZero(amount);
		try {
			if (lock.tryLock(20L, TimeUnit.MILLISECONDS)) {
				try {
					if (balance.compareTo(amount) > 0) {
						balance = balance.subtract(amount);
						return true;
					}
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return false;
	}

	@Override
	public boolean credit(BigDecimal amount) {
		Objects.requireNonNull(amount, "Amount cannot be null");
		Validator.amountNotNegativeOrZero(amount);

		try {
			if (lock.tryLock(20L, TimeUnit.MILLISECONDS)) {
				try {
					balance = balance.add(amount);
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return true;
	}

	@Override
	public Lock writeLock() {
		return lock;
	}

}
