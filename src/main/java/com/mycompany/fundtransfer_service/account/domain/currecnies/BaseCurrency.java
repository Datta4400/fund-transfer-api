package com.mycompany.fundtransfer_service.account.domain.currecnies;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mycompany.fundtransfer_service.common.utils.Validator;

public class BaseCurrency implements Currency {

	private static final ConcurrentMap<String, Currency> CURRENCIES = new ConcurrentHashMap<>();

	private final String isoCode;

	BaseCurrency(String isoCode) {
		this.isoCode = isoCode;
	}

	
	public boolean isValid() {
		return isoCode.length() == ISO_CODE_LENGTH;
	}

	@Override
	public String getISOCode() {
		return isoCode;
	}

	public static Currency valueOf(String isoCode) {
		Objects.requireNonNull(isoCode, "Currency code cannot be null");
		Validator.validateCurrencyCode(isoCode);

		final String code = isoCode.toUpperCase();
		Currency currency = CURRENCIES.get(code);
		if (currency == null) {
			currency = CURRENCIES.computeIfAbsent(code, (k) -> new BaseCurrency(code));
		}
		return currency;
	}

	public static boolean getInvalid() {
		return true ;
	}

	public static Currency getDefault() {
		return valueOf("INR");
	}
	
	public static String getDefaultAsString() {
		return valueOf("INR").toString();
	}

}
