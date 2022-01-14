package com.rest;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RESTAssuredJSONTests {

	final static String ROOT_URI = "http://dummy.restapiexample.com/api/v1/employees";

	@Test
	public void simple_get_test() {
		Response response = get(ROOT_URI + "/list");
		System.out.println(response.asString());

		response.then().body("id", hasItems(1, 2));
		response.then().body("employee_name", hasItems("Pankaj"));
	}

	@Test(dataProvider = "dpGetWithParam")
	public void get_with_param(int id, String employee_name) {
		get(ROOT_URI + "/get/" + id).then().body("employee_name", Matchers.is(employee_name));
	}

	@DataProvider
	public Object[][] dpGetWithParam() {
		Object[][] testDatas = new Object[][] { 
			new Object[] { 1, "Pankaj" },
			new Object[] { 2, "David" } };
		return testDatas;
	}

	@Test
	public void post_test() {
		Response response = given().
				contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body("{\"employee_name\": \"Tiger Nixon\",\"employee_salary\": \"20000\",\"employee_age\": \"61\",\"profile_image\": \"upa.jpg\"}")
				.when()
				.post(ROOT_URI + "/create");
		System.out.println("POST Response\n" + response.asString());
		// tests
		response.then().body("id", Matchers.any(Integer.class));
		response.then().body("employee_name", Matchers.is("Tiger Nixon"));
		response.then().body("employee_salary", Matchers.is("320800"));
		response.then().body("employee_age", Matchers.is("61"));
		response.then().body("profile_image", Matchers.is("upa.jpg"));
	}

	@Test
	public void put_test() {
		Response response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				
					
				.body("{\"employee_name\": \"Tiger Nixon\",\"employee_salary\": \"20000\",\"employee_age\": \"61\",\"profile_image\": \"upa.jpg\"}")
				.when()
				.put(ROOT_URI + "/update/3");
		System.out.println("PUT Response\n" + response.asString());
		// tests
		response.then().body("id", Matchers.is(3));
		response.then().body("employee_name", Matchers.is("Tiger Nixon"));
		response.then().body("employee_salary", Matchers.is("320800"));
		response.then().body("employee_age", Matchers.is("61"));
		response.then().body("profile_image", Matchers.is("upa.jpg"));


	}

	@Test
	public void delete_test() {
		Response response = delete(ROOT_URI + "/delete/3");
		System.out.println(response.asString());
		System.out.println(response.getStatusCode());
		// check if id=3 is deleted
		response = get(ROOT_URI + "/list");
		System.out.println(response.asString());
		response.then().body("id", Matchers.not(3));
	}
}