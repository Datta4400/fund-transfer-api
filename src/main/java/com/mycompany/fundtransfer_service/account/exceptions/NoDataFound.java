package com.mycompany.fundtransfer_service.account.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NoDataFound extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final Long id;
	private final String msg;

	public NoDataFound(Long id, String msg) {
		this.id = id;
		this.msg = msg;
	}

}
