package com.mycompany.fundtransfer_service.account.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TransactionDTO {
	 private  Long debitAccountId;
	 private  Long creditAccountId;
	 private  BigDecimal amount;
	 

}
