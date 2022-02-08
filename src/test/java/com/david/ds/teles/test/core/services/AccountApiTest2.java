package com.david.ds.teles.test.core.services;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import com.david.ds.teles.core.domain.Account;
import com.david.ds.teles.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusMock;
import io.smallrye.mutiny.Uni;
import java.time.OffsetDateTime;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AccountApiTest2 {
	@Inject
	AccountRepository repo;

	@Inject
	ObjectMapper mapper;

	@BeforeAll
	public static void setup() {
		// or can use the @InjectMock
		AccountRepository mockRepo = Mockito.mock(AccountRepository.class);
		QuarkusMock.installMockForType(mockRepo, AccountRepository.class);
	}

	public List<Account> generateDefaultList() {
		List<Account> accounts = List.of(
			new Account(1l, "david.ds.teles@gmail.com", "google", OffsetDateTime.now())
		);
		return accounts;
	}

	@Test
	public void findall_endpoint_should_return_account_list() throws JsonProcessingException {
		List<Account> accountList = generateDefaultList();
		Mockito.when(repo.getAll()).thenReturn(Uni.createFrom().item(accountList));

		String accountsJSON = mapper.writeValueAsString(accountList);

		given().when().get("/all").then().statusCode(200).body(is(accountsJSON));
	}

	@Test
	public void create_endpoint_should_return_created_account() throws JsonProcessingException {
		Account account = new Account();
		account.setEmail("david.ds.teles@gmail.com");

		Account accountResult = new Account(1l, account.getEmail(), "", null);

		Mockito
			.when(repo.persist(Mockito.any(Account.class)))
			.thenReturn(Uni.createFrom().item(accountResult));

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
	public void update_endpoint_should_update_an_account() throws JsonProcessingException {
		Account account = new Account(1l, "david.ds.teles@gmail.com", "google", null);

		Mockito
			.when(repo.update(Mockito.any(), Mockito.anyCollection()))
			.thenReturn(Uni.createFrom().item(1));

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
}
