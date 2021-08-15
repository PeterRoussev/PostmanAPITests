package rest;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomateGetTest {

    private static RequestSpecification requestSpecification;

    @BeforeAll
    static void beforeAll(){
        requestSpecification = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "{{apiKey}}");
    }

    @Test
    public void validateGetStatusCode() {
        given(requestSpecification).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body("workspaces.name", hasItems("My Workspace", "MyLastWorkspace", "MyNewWorkspace"),
                        "workspaces.type", hasItems("personal"));
    }

    @Test
    public void extractResponse() {
       Response response = given(requestSpecification).

        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().statusCode(200).extract().response();
        System.out.println("Response is: " + response.getHeader("Date"));
     }

    @Test
    public void hamcrestAssertOfExtractedResponse(){
        String name =
         given(requestSpecification).
         when().
                get("/workspaces").
         then().
                log().all().
                assertThat().statusCode(200).
                extract().response().path("workspaces[0].name");
        assertThat(name, equalTo("MyNewWorkspace"));

    }

    @Test
    public void validateResponseBodyHamcrest(){
        given(requestSpecification).
         when().
                get("/workspaces").
         then().
                log().all().
                assertThat().statusCode(200).
                body("workspaces.name", containsInAnyOrder( "MyLastWorkspace", "MyNewWorkspace", "My Workspace" ),
                        "workspaces[0]", hasKey("type"),
                        "workspaces[0]", hasEntry("name", "MyNewWorkspace"));
    }

    @Test

    public void mockServerHeadersTest(){
        Map<String, String> headers = new HashMap<>();
        headers.put("x-mock-match-request-headers", "header");
        headers.put("header", "value2");
        Header header1 = new Header("x-mock-match-request-headers", "header");
        Header header2 = new Header("header", "value2");
        given().
                baseUri("https://24405aed-dd45-4268-b84a-c160a22b3ec7.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test

    public void assertResponseHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("x-mock-match-request-headers", "header");
        headers.put("header", "value2");

        given().
                baseUri("https://24405aed-dd45-4268-b84a-c160a22b3ec7.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200).
                header("headerResponse", "resValue2");

    }

    @Test

    public void printExtractedHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("x-mock-match-request-headers", "header");
        headers.put("header", "value2");

        Headers extractedHeaders = given().
                baseUri("https://24405aed-dd45-4268-b84a-c160a22b3ec7.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200).
                header("headerResponse", "resValue2").
                extract().headers();

        for(Header header : extractedHeaders){
            System.out.println(header.getName() + " --- " + header.getValue());
        }
    }

    @Test

    public void requestSpecificationsUsage(){

        given(requestSpecification).
        when().
                get("/workspaces").
        then().
                log().all().
                assertThat().statusCode(200).
                body("workspaces.name", containsInAnyOrder( "MyLastWorkspace", "MyNewWorkspace", "My Workspace" ),
                        "workspaces[0]", hasKey("type"),
                        "workspaces[0]", hasEntry("name", "MyNewWorkspace"));
    }

    @Test

    public void nonBDD(){
        Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.path("workspaces[0].name").toString(), is(equalTo("MyNewWorkspace")));
        assertThat(response.statusCode(), is(equalTo(200)));
    }
}
