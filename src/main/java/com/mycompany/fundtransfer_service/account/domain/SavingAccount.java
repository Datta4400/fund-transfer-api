package com.mycompany.fundtransfer_service.account.domain;

import java.math.BigDecimal;

public class SavingAccount extends AbstractAccount {

	SavingAccount(Long id, String currency, String number, BigDecimal balance) {
		super(id, currency, number, balance);

	}

}
