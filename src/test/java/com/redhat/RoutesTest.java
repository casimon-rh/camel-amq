package com.redhat;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;

@QuarkusTest
public class RoutesTest {

  @Test
  public void testFruitsEndpoint() {
    given()
        .when().get("/fruits/")
        .then()
        .statusCode(200)
        .body("$.size()", Matchers.is(1),
            "name", Matchers.contains("Apple"));
    given()
        .when().get("/fruits/1")
        .then()
        .statusCode(200)
        .body("name", Matchers.contains("Apple"));

  }
}
