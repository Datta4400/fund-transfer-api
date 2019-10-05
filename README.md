# Money transfer Rest API

A Java RESTful API for fund transfers between accounts <br/>
#### Default supported currency is "INR"

### Technologies
- Java 8
- Maven
- Spark Framework (with embedded Jetty)
- Lombok
- google/gson
- Hand-written in-memory data storage using concurrency utilities
- JUnit 5
- Rest-assured (for unit testing)

### How to run
```sh
- java -jar ".\target\fundtransfer-service-0.0.1-SNAPSHOT.jar
```

Application starts a jetty server on localhost port 8080 An memory datastore initialized with some sample accounts data To view

- http://localhost:8080/account
- http://localhost:8080/transaction

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /account | get all accounts |
| GET | /transaction | get all transactions between accounts |
| POST | /transaction | perform transaction between 2 accounts |

### Http Status
- 200 OK
- 201 CREATED
- 400 Bad request
- 404 Not found

### Sample JSON for transaction

#### User Transaction:
```sh
{
	"debitAccountId" : 4,
	"creditAccountId": 2,
	"amount": 60000
}
```
