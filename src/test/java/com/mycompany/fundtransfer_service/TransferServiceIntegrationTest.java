package com.mycompany.fundtransfer_service ;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.eclipse.jetty.http.HttpStatus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class TransferServiceIntegrationTest {

	@BeforeEach
     void setUp() {
        FundTransfer.start();
    }

    @AfterEach
     void tearDown() {
    	FundTransfer.stop();
    }
    
	@Test
	public void given_valid_details_should_transfer_fund() {
		RestAssured.baseURI = "http://localhost:8080";
		String response = given().header("Content-Type", "application/json")
				.body("{\r\n\t\"debitAccountId\" : 1,\r\n\t\"creditAccountId\":2,\r\n\t\"amount\": 100\r\n}").when()
				.post("/transaction").then().assertThat().statusCode(HttpStatus.CREATED_201).extract().asString();

		assertTrue(response.contains("COMPLETED"));

	}
}
