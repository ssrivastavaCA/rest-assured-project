import io.qameta.allure.Link;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.JiraCreateIssueResponse;
import util.PayloadUtil;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Link("SKP-2")
public class JsonSchemaValidationTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://sivakarthikpara.atlassian.net";
    }

    @Test
    public void createJiraTask() {

        Response response = given().filter(new AllureRestAssured())
                .log().all()
                .accept("application/json")
                .contentType("application/json")
                .auth().preemptive().basic("mail@mail.com", "token")
                .body(PayloadUtil.readJsonFile("src/main/resources/json.models/jira-create-issue.json"))
                .when().post("/rest/api/3/issue");

        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json.models/jira-create-or-update-schema.json"))
                .log().all();

        JiraCreateIssueResponse jiraResponse = response.body().as(JiraCreateIssueResponse.class);

        assertThat(jiraResponse.getId()).matches("\\d+");
        assertThat(jiraResponse.getKey()).matches("PYMT-\\d+");
        assertThat(jiraResponse.getSelf()).matches("https://sivakarthikpara.atlassian.net/rest/api/3/issue/\\d+");

        System.out.println(jiraResponse);
    }

}
