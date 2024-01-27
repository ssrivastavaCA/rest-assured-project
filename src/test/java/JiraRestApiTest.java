import io.qameta.allure.Link;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static jira.JiraRestApiKeywords.*;
import static org.assertj.core.api.Assertions.assertThat;

@Link("SKP-2")
public class JiraRestApiTest {

    String description = "This is the description. --> https://sivakarthikpara.atlassian.net/rest/api/3/issue";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://sivakarthikpara.atlassian.net";
        RestAssured.preemptive().basic("sivakarthikpara@gmail.com", "SAfdrCGxzXiPu7YGSsX5A3CB");
    }

    @Test
    public void createAndDeleteJiraTask() {
        Response jiraIssue = createIssue();
        jiraIssue.then().log().all();
        JsonPath jiraResponse = new JsonPath(jiraIssue.asInputStream());
        String jiraId = jiraResponse.getString("key");
        assertThat(jiraIssue.statusCode()).isEqualTo(201);

        Response getJira = getJiraIssue(jiraId);
        jiraResponse = new JsonPath(getJira.asInputStream());
        assertThat(getJira.statusCode()).isEqualTo(200);
        assertThat(jiraResponse.getString("fields.description.content[0].content[0].text")).isEqualTo(description);

        Response deleteJira = deleteJiraIssue(jiraId);
        assertThat(deleteJira.statusCode()).isEqualTo(204);
    }

}
