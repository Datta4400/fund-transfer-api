package com.mycompany.fundtransfer_service.transaction.domain;

public enum TransactionState {
	NEW,
    INSUFFICIENT_FUNDS,
    COMPLETED,
    CONCURRENCY_ERROR,
    RESTARTED
}
