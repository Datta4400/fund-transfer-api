
package com.mycompany.fundtransfer_service.common.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.domain.currecnies.Currency;

public class Validator {

	public static void validateNumber(String number) {
		if (StringUtils.length(number) != 5 || !StringUtils.isAlphanumeric(number)) {
			throw new IllegalArgumentException("Please enter valid account number");
		}

	}

	public static void amountNotNegativeOrZero(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Amount must be a positive value");
		}
	}

	public static void validateCurrencyIsTheSame(Account debit, Account credit) {
		if (!debit.getCurrency().equals(credit.getCurrency())) {
			throw new UnsupportedOperationException("Only single currency supported");
		}
	}

	public static void validateAccountIsDifferent(Account debit, Account credit) {
		if (debit.equals(credit)) {
			throw new IllegalArgumentException("Credit and Debit account must be diffrent");
		}
	}

	public static void validateCurrencyCode(String isoCode) {
		if (isoCode.length() != Currency.ISO_CODE_LENGTH) {
			throw new IllegalArgumentException(
					"Currency code have to have length equals to " + Currency.ISO_CODE_LENGTH);
		}
	}

}
