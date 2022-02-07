package com.david.ds.teles.core.services;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class AccountServiceTest {

	@Test
	public void test_findall_endpoint_with_result_200() {
		given().when().get("/account/all").then().statusCode(200);
	}
	//	@Test
	//	public void testGreetingEndpoint() {
	//		String uuid = UUID.randomUUID().toString();
	//		given().pathParam("name", uuid).when().get("/hello/greeting/{name}").then().statusCode(200)
	//				.body(is("hello " + uuid));
	//	}
}
