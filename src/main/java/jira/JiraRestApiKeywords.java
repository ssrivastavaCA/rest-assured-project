package jira;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import util.PayloadUtil;

import static io.restassured.RestAssured.given;

public class JiraRestApiKeywords {

    private static final String CREATE_ISSUE = "/rest/api/3/issue";
    private static final String GET_ISSUE = "/rest/api/3/issue/{}";
    private static final String USERNAME = "user@text.com";
    private static final String API_TOKEN = "zAYuAuGQxmJTO5hBheUV772F";

    @Step
    public static Response createIssue() {
        return given().filter(new AllureRestAssured())
                .log().all()
                .accept("application/json")
                .contentType("application/json")
                .auth().basic(USERNAME, API_TOKEN)
                .body(PayloadUtil.readJsonFile("src/main/resources/json.models/jira-create-issue.json"))
                .when().post(CREATE_ISSUE);
    }

    @Step
    public static Response getJiraIssue(String jiraId) {
        return given().filter(new AllureRestAssured())
                .auth().basic(USERNAME, API_TOKEN)
                .when().get(GET_ISSUE, jiraId);
    }

    @Step
    public static Response deleteJiraIssue(String jiraId) {
        return given().filter(new AllureRestAssured())
                .auth().basic(USERNAME, API_TOKEN)
                .when().delete(GET_ISSUE, jiraId);
    }

}
