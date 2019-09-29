

package com.mycompany.fundtransfer_service.account.repository ;


import java.math.BigDecimal;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.domain.currecnies.Currency;
import com.mycompany.fundtransfer_service.common.Repository;

public interface AccountsRepository  extends Repository<Account>{

    Account addBankAccount(Currency currency, String number, BigDecimal balance);

    Account addBankAccount(String number, BigDecimal balance);

    BigDecimal getBalance(Long id);

  
}