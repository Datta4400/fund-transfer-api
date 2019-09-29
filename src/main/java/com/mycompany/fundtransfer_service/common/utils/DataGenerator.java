
package com.mycompany.fundtransfer_service.common.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.fundtransfer_service.account.repository.RepositoryFactory;

public class DataGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);

	private final RepositoryFactory factory;

	private DataGenerator(RepositoryFactory factory) {
		this.factory = factory;
	}

	public void generate() {

		factory.getAccountsRepository().addBankAccount("10001", BigDecimal.valueOf(5000L));
		factory.getAccountsRepository().addBankAccount("10002", BigDecimal.valueOf(1000L));
		factory.getAccountsRepository().addBankAccount("10003", BigDecimal.valueOf(80000L));
		factory.getAccountsRepository().addBankAccount("10004", BigDecimal.valueOf(50000L));

	}

	public static DataGenerator getInstance(RepositoryFactory facory) {
		return new DataGenerator(facory);
	}
}
