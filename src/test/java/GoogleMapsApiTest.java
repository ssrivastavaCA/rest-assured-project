import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.PayloadUtil;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GoogleMapsApiTest {

    String place_id;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

    @Test(priority = 1)
    public void postPlace() {
        String payload = PayloadUtil.addPlaceBody();
        Response response = given().accept("application/json").contentType("application/json")
                .queryParam("key", "qaclick123")
                .when().body(payload).post("/maps/api/place/add/json");
//        response.prettyPeek();
        response.then().log().all();
        response.then().body("scope", equalTo("APP"));

        JsonPath jsonPath = new JsonPath(response.asInputStream());
        System.out.println(jsonPath.get().toString());
        place_id = jsonPath.getString("place_id");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(jsonPath.getString("scope")).isEqualTo("APP");
        assertThat(jsonPath.getString("status")).isEqualTo("OK");
        assertThat(place_id).isNotNull();
    }

    @Test(priority = 2)
    public void getCreatedPlace() {
        Response response = given().accept("*/*").contentType("application/json")
                .queryParam("place_id", place_id)
                .queryParam("key", "qaclick123")
                .when().get("/maps/api/place/get/json");
        response.then().log().all();

        JsonPath jsonPath = new JsonPath(response.asInputStream());
        System.out.println(jsonPath.get().toString());

        assertThat(jsonPath.get("location.latitude").toString()).isEqualTo("-38.383494");
        assertThat(jsonPath.get("location.longitude").toString()).isEqualTo("33.427362");
        assertThat(jsonPath.get("website").toString()).isEqualTo("http://google.com");
    }

    @Test(priority = 3)
    public void updatePlace() {
        Response response = given().queryParam("place_id", place_id + "&key=qaclick123")
                .body(PayloadUtil.updatePlaceBody(place_id))
                .when().put("/maps/api/place/update/json");
        response.prettyPeek();
        JsonPath jsonPath = new JsonPath(response.asInputStream());
        System.out.println(jsonPath.get().toString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(jsonPath.get("msg").toString()).isEqualTo("Address successfully updated");
    }

    @Test(priority = 4)
    public void deletePlace() {
        Response response = given().queryParam("key", "qaclick123")
                .body(PayloadUtil.deletePlaceBody(place_id)).delete("/maps/api/place/delete/json");

        response.prettyPeek();
        JsonPath jsonPath = new JsonPath(response.asInputStream());
        System.out.println(jsonPath.get().toString());
        //JsonPath.from(response.asInputStream()).get("status")
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(jsonPath.get("status").toString()).isEqualTo("OK");
    }

}
