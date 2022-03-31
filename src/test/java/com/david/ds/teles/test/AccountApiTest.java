package com.david.ds.teles.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.david.ds.teles.api.AccountApi;
import com.david.ds.teles.core.domain.Account;
import com.david.ds.teles.repository.AccountRepository;
import com.david.ds.teles.test.resources.TestContainerResourceLifecycleManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import java.time.OffsetDateTime;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTestResource(TestContainerResourceLifecycleManager.class)
@QuarkusTest
@TestHTTPEndpoint(AccountApi.class)
@TestMethodOrder(OrderAnnotation.class)
public class AccountApiTest {
	@Inject
	AccountRepository repo;

	@Inject
	ObjectMapper mapper;

	@BeforeAll
	public static void setup() {}

	public List<Account> generateDefaultList() {
		List<Account> accounts = List.of(
			new Account(1l, "david.ds.teles@gmail.com", "google", OffsetDateTime.now())
		);
		return accounts;
	}

	@Test
	@Order(1)
	public void create_endpoint_should_return_created_account() throws JsonProcessingException {
		// given
		Account account = new Account();
		account.setEmail("david.ds.teles@gmail.com");
		String body = mapper.writeValueAsString(account);

		// when
		ValidatableResponse response = given()
			.header("Content-type", "application/json")
			.and()
			.body(body)
			.when()
			.post()
			.then();

		// then
		response.statusCode(201);
		response.body("id", is(1), "email", is(account.getEmail()));
	}

	@Test
	public void should_give_error_when_try_create_account_with_invalid_data()
		throws JsonProcessingException {
		// given
		Account account = new Account();
		String body = mapper.writeValueAsString(account);

		String resultMessage = "The data provided is invalid";
		String resultDetail = "save.account.email deve ser informado. Valor informado ";

		// when
		ValidatableResponse response = given()
			.header("Content-type", "application/json")
			.and()
			.body(body)
			.when()
			.post()
			.then();

		// then
		response.statusCode(400);
		response.body(
			"message",
			is(resultMessage),
			"details.size()",
			is(1),
			"details[0]",
			is(resultDetail)
		);
	}

	@Test
	public void should_give_error_when_try_create_account_with_invalid_email_provider()
		throws JsonProcessingException {
		// given
		Account account = new Account();
		account.setEmail("david.ds.teles@foo.com");

		String resultMessage = "O email \"" + account.getEmail() + "\" informado é inválido";

		// when
		ValidatableResponse response = given()
			.header("Content-type", "application/json")
			.header("accept-language", "pt_BR")
			.and()
			.body(account)
			.when()
			.post()
			.then();

		// then
		String bodyRsp = response.extract().body().asPrettyString();
		System.out.println(bodyRsp);
		response.statusCode(400);
		response.body("message", is(resultMessage));
	}

	@Test
	@Order(2)
	public void update_endpoint_should_update_an_account() throws JsonProcessingException {
		// given
		Account account = new Account(1l, "david.ds.teles@gmail.com", "google", null);

		// when
		ValidatableResponse response = given()
			.header("Content-type", "application/json")
			.and()
			.body(account)
			.when()
			.put()
			.then();

		// then
		response.statusCode(204);
	}

	@Test
	@Order(3)
	public void findall_endpoint_should_return_account_list() throws JsonProcessingException {
		// when
		ValidatableResponse response = given().when().get("/all").then();

		// then
		response.statusCode(200);
		response.body(
			"$.size()",
			is(1),
			"[0].id",
			is(1),
			"[0].email",
			is("david.ds.teles@gmail.com"),
			"[0].updatedAt",
			is(notNullValue())
		);
	}
}
