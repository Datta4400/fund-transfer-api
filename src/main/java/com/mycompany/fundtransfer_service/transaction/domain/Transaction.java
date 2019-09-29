

package com.mycompany.fundtransfer_service.transaction.domain;



import java.math.BigDecimal;

import com.mycompany.fundtransfer_service.account.domain.Account;

public interface Transaction {

    Account getDebit();

    Account getCredit();

    BigDecimal getAmount();
    
    Long getId();

    boolean run();
    
    TransactionState getState();
}
