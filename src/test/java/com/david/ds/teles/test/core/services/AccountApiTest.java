package com.david.ds.teles.test.core.services;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import com.david.ds.teles.api.AccountApi;
import com.david.ds.teles.core.domain.Account;
import com.david.ds.teles.repository.AccountRepository;
import com.david.ds.teles.test.resources.TestContainerResourceLifecycleManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import java.time.OffsetDateTime;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
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
		Account account = new Account();
		account.setEmail("david.ds.teles@gmail.com");

		Account accountResult = new Account(1l, account.getEmail(), null, null);

		String body = mapper.writeValueAsString(account);
		String result = mapper.writeValueAsString(accountResult);

		given()
			.header("Content-type", "application/json")
			.and()
			.body(body)
			.when()
			.post()
			.then()
			.statusCode(201)
			.body(is(result));
	}

	@Test
	@Order(2)
	public void update_endpoint_should_update_an_account() throws JsonProcessingException {
		Account account = new Account(1l, "david.ds.teles@gmail.com", "google", null);

		String body = mapper.writeValueAsString(account);

		given()
			.header("Content-type", "application/json")
			.and()
			.body(body)
			.when()
			.put()
			.then()
			.statusCode(204);
	}

	@Test
	@Order(3)
	public void findall_endpoint_should_return_account_list() throws JsonProcessingException {
		ResponseBody<?> responseBody = given()
			.when()
			.get("/all")
			.then()
			.statusCode(200)
			.extract()
			.response()
			.body();
		List<Account> result = mapper.readValue(
			responseBody.asString(),
			new TypeReference<List<Account>>() {}
		);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(1l, result.get(0).getId());
		Assertions.assertEquals("david.ds.teles@gmail.com", result.get(0).getEmail());
		Assertions.assertEquals("google", result.get(0).getProvider());
		Assertions.assertNotNull(result.get(0).getUpdatedAt());
	}
}
