package com.mycompany.fundtransfer_service.transaction.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.common.utils.Validator;

public class MoneyTransaction implements Transaction {
	private static final Logger logger = LoggerFactory.getLogger(MoneyTransaction.class);

	private final Long id;
	private final Account debit;
	private final Account credit;
	private final BigDecimal amount;
	private TransactionState state;

	public MoneyTransaction(Long id, Account debit, Account credit, BigDecimal amount) {
		Objects.requireNonNull(id, "Id cannot be null");
		Objects.requireNonNull(debit, "Debit account cannot be null");
		Objects.requireNonNull(credit, "Credit account cannot be null");
		Objects.requireNonNull(amount, "Amount cannot be null");
		Validator.amountNotNegativeOrZero(amount);
		Validator.validateAccountIsDifferent(debit, credit);
		Validator.validateCurrencyIsTheSame(debit, credit);

		this.id = id;
		this.debit = debit;
		this.credit = credit;
		this.amount = amount;
		this.state = TransactionState.NEW;

	}

	public Long getId() {
		return id;
	}

	@Override
	public Account getDebit() {
		return debit;
	}

	@Override
	public Account getCredit() {
		return credit;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public synchronized boolean run() {
		if (state != TransactionState.COMPLETED) {
			changeState();
			return doRun();
		}
		return false;
	}

	private void changeState() {
		switch (state) {
		case INSUFFICIENT_FUNDS:
			state = TransactionState.INSUFFICIENT_FUNDS ; 
		case CONCURRENCY_ERROR:
			state = TransactionState.RESTARTED;
			break;
		default:
			break;
		}
	}

	private boolean doRun() {
		final Lock debitLock = debit.writeLock();
		try {
			if (debitLock.tryLock(20L, TimeUnit.MILLISECONDS)) {
				try {
					final Lock creditLock = credit.writeLock();
					if (creditLock.tryLock(20L, TimeUnit.MILLISECONDS)) {
						try {
							if (debit.debit(amount)) {
								if (credit.credit(amount)) {
									state = TransactionState.COMPLETED;
									logger.trace("Transaction {} completed", id);
									return true;
								}
							}else {
								state = TransactionState.INSUFFICIENT_FUNDS;
								return false ;
							}
						} finally {
							creditLock.unlock();
						}
					}
				} finally {
					debitLock.unlock();
				}
			}
		} catch (InterruptedException e) {
			state = TransactionState.CONCURRENCY_ERROR;
			logger.error(e.getLocalizedMessage(), e);
		}
		return false;
	}

	public static Transaction execute(Long id, Account debit, Account credit, BigDecimal amount) {
		return new MoneyTransaction(id, debit, credit, amount);
	}

	@Override
	public TransactionState getState() {
		return state;
	}

}
