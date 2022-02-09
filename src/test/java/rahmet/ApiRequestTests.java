package rahmet;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;


public class ApiRequestTests {

    private String body;

    @BeforeAll
    static void url() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void singleResource() {

        Response response = get("api/unknown/2")
                .then()
                .statusCode(200)
                .extract().response();

        assertThat(response.path("data.name").toString()).isEqualTo("fuchsia rose");
        assertThat(response.path("data.id").toString()).isEqualTo("2");
        assertThat(response.path("data.year").toString()).isEqualTo("2001");
        assertThat(response.path("support.url").toString()).isEqualTo("https://reqres.in/#support-heading");

    }

    @Test
    void createUser(){
        body = "{ \"name\": \"morpheus\" , \"job\": \"leader\"}";

        given()
                .contentType(JSON)
                .body(body)
                .when()
                .post("api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"));



    }

    @Test
    void updateUser(){
        body = "{ \"name\": \"morpheus\" , \"job\": \"QA\"}";

        given()
                .contentType(JSON)
                .body(body)
                .when()
                .put("api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"), "job", is("QA"));
    }

    @Test
    void registerSuccessful(){
        body = "{ \"email\": \"eve.holt@reqres.in\" , \"password\": \"pistol\"}";

        given()
                .contentType(JSON)
                .body(body)
                .when()
                .post("api/register")
                .then()
                .statusCode(200)
                .body("id", is(4), "token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void deleteTest(){
        given()
                .contentType(JSON)
                .when()
                .delete("api/users/2")
                .then()
                .statusCode(204);
    }

}
