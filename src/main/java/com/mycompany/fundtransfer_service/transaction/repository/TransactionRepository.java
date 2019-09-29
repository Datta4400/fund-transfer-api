package com.mycompany.fundtransfer_service.transaction.repository;

import java.math.BigDecimal;
import java.util.Collection;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.common.Repository;
import com.mycompany.fundtransfer_service.transaction.domain.Transaction;



public interface TransactionRepository extends Repository<Transaction>{
	  Transaction add(Account debit, Account credit, BigDecimal amount);

	  Collection<Transaction> getByAccount(Account account);

}
