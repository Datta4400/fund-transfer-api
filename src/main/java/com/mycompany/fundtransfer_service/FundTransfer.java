
package com.mycompany.fundtransfer_service;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.fundtransfer_service.account.domain.Account;
import com.mycompany.fundtransfer_service.account.dto.TransactionDTO;
import com.mycompany.fundtransfer_service.account.exceptions.NoDataFound;
import com.mycompany.fundtransfer_service.common.Repository;
import com.mycompany.fundtransfer_service.common.utils.JsonUtils;
import com.mycompany.fundtransfer_service.common.utils.TransferService;
import com.mycompany.fundtransfer_service.transaction.domain.Transaction;

import spark.Response;
import spark.Spark;

public final class FundTransfer {

	private static final Logger logger = LoggerFactory.getLogger(FundTransfer.class);

	static void start() {

		FundTransfer.main(null);
		Spark.awaitInitialization();
	}

	static void startWithData() {
		FundTransfer.main(null);
		Spark.awaitInitialization();
	}

	static void stop() {
		Spark.stop();
		Spark.awaitStop();
	}

	public static void main(String[] args) {
		TransferService.getInstance().generateData();
		Spark.port(8080);
		Spark.threadPool(20);
		Spark.after((req, res) -> res.type("application/json"));
		accountsRoutes();
		transactionRoutes();
		exceptionsHandling();

	}

	private static void accountsRoutes() {
		Spark.get("/account", (req, res) -> {

			final Repository<Account> repository = TransferService.getInstance().getFactory().getAccountsRepository();
			return JsonUtils.build().toJson(repository.getAll());
		});
	}

	private static void transactionRoutes() {

		Spark.get("/transaction", (req, res) -> {

			final Repository<Transaction> repository = TransferService.getInstance().getFactory()
					.getTransactionRepository();
			return JsonUtils.build().toJson(repository.getAll());
		});

		Spark.post("/transaction", (req, res) -> {
			logger.trace(String.format("/transaction API has been called with payload", req.body()));
			final TransactionDTO tranferPayload = JsonUtils.build().fromJson(req.body(), TransactionDTO.class);
			final Transaction transaction = TransferService.getInstance().transfer(tranferPayload);
			res.status(HttpServletResponse.SC_CREATED);
			res.header("Location", "/transaction/" + transaction.getId());
			return JsonUtils.build().toJson(transaction);
		});
	}

	private static void exceptionsHandling() {

		Spark.exception(NoDataFound.class,
				(e, req, res) -> setErrorInfo(res, e.getMsg(), HttpServletResponse.SC_BAD_REQUEST));

		Spark.exception(NullPointerException.class,
				(e, req, res) -> setErrorInfo(res, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR));

		Spark.exception(NumberFormatException.class,
				(e, req, res) -> setErrorInfo(res, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST));

		Spark.exception(NoSuchElementException.class,
				(e, req, res) -> setErrorInfo(res, e.getMessage(), HttpServletResponse.SC_NOT_FOUND));
	}

	private static void setErrorInfo(Response res, String errMessage, int errCode) {
		res.status(errCode);
		res.body(JsonUtils.toJson(errMessage, errCode));
	}

}
