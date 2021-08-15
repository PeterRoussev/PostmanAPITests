package rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
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

public class AutomateRestPut {
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

    public void validatePutNonBDD(){
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"AnotherNewWorkspace\",\n" +
                "        \"type\": \"personal\"\n" +
                "    }\n" +
                "}";
        String id = "ab4c35c8-6890-4591-b547-de7fcd09edfd";
        Response response = with().body(payload).put("/workspaces/" + id);
        assertThat(response.path("workspace.id"), equalTo(id));
        assertThat(response.path("workspace.name"), equalTo("AnotherNewWorkspace"));
    }

    @Test

    public void validateDeleteTest(){
        String id = "ab4c35c8-6890-4591-b547-de7fcd09edfd";
                given().
                when().
                delete("/workspaces/" + id).then().assertThat().body("workspace.id", equalTo(id));
    }

}
