package com.mycompany.fundtransfer_service.account.domain;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;


public interface Account {

	String getNumber();
	
	Long getId();

	String getCurrency();

	BigDecimal getBalance();

	boolean debit(BigDecimal amount);

	boolean credit(BigDecimal amount);

	Lock writeLock();
	
	
}
