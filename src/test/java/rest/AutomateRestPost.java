package rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomateRestPost {

    @BeforeAll

    public static void beforeAll(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.postman.com").
                addHeader("X-Api-Key", "PMAK-6116408d35ec5e005c7d3046-2aeb1203aac985ced9f97e35fe6da34c9d").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder =  new ResponseSpecBuilder().
                expectContentType(ContentType.JSON).
                expectStatusCode(200).
                log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test

    public void validatePostRequestBDD(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"AnotherWorkspace3\",\n" +
                "        \"type\": \"personal\"\n" +
                "    }\n" +
                "}";
       Response response = given().body(payload).
        when().
                post("/workspaces").
        then().
                assertThat().
                body("workspace.name", equalTo("AnotherWorkspace3"),
                        "workspace.id", matchesPattern("^[a-zA-Z0-9-]{36}$")).extract().response();

       String id = response.getBody().asString();
        System.out.println(id);
        }

    @Test

    public void validatePostRequestNonBDD(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"AnotherWorkspace3\",\n" +
                "        \"type\": \"personal\"\n" +
                "    }\n" +
                "}";
        Response response = with().body(payload).post("/workspaces");
        assertThat(response.path("workspace.name"), equalTo("AnotherWorkspace3"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-zA-Z0-9-]{36}$"));
    }
}


