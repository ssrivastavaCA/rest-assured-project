import io.qameta.allure.Link;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.PayloadUtil;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Link("SKP-1")
public class ParameterizedBooksTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
    }

    @DataProvider
    public Object[][] bookDataProvider() {
        return new Object[][]{
                {"Geometry", "A", "1", "Euclid"},
                {"Coordinate Geometry", "B", "2", "Rene Descartes"},
                {"Medicine", "C", "3", "Sushruta"},
                {"Philosophy", "D", "4", "Chanakya"}
        };
    }

    @Test(dataProvider = "bookDataProvider", priority = 1)
    public void addBook(String bookTitle, String bookIsbn, String aisle, String bookAuthor) {
        Map<String, String> bookMap = new HashMap<>();
        bookMap.put("name", bookTitle);
        bookMap.put("isbn", bookIsbn);
        bookMap.put("aisle", aisle);
        bookMap.put("author", bookAuthor);

        Response response = given().filter(new AllureRestAssured()).log().all().body(PayloadUtil.addBookBody(bookMap)).contentType("application/json")
                .when().post("/Library/Addbook.php");

        response.prettyPeek();
        JsonPath jsonPath = new JsonPath(response.asInputStream());
        assertThat(response.statusCode()).isEqualTo(200);
        System.out.println(jsonPath.getString("Msg"));
        assertThat(jsonPath.getString("Msg")).isEqualTo("successfully added");
        assertThat(jsonPath.getString("ID")).isEqualTo(bookIsbn + aisle);
    }

    @Test(dataProvider = "bookDataProvider", priority = 2)
    public void getBookDetails(String bookTitle, String bookIsbn, String aisle, String bookAuthor) {
        Response response = given().filter(new AllureRestAssured()).queryParam("ID", bookIsbn + aisle).log().all()
                .when().get("/Library/GetBook.php");

        response.prettyPeek();
        JsonPath jsonPath = new JsonPath(response.asInputStream());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(jsonPath.getString("[0].book_name")).isEqualTo(bookTitle);
        assertThat(jsonPath.getString("[0].isbn")).isEqualTo(bookIsbn);
        assertThat(jsonPath.getString("[0].aisle")).isEqualTo(aisle);
        assertThat(jsonPath.getString("[0].author")).isEqualTo(bookAuthor);
    }

    @Test(dataProvider = "bookDataProvider", priority = 3)
    public void deleteBookByID(String bookTitle, String bookIsbn, String aisle, String bookAuthor) {
        Response response = given().filter(new AllureRestAssured()).log().all()
                .body("{\"ID\": \"" + bookIsbn + aisle + "\"}")
                .when().delete("/Library/DeleteBook.php");

        response.prettyPeek();
        JsonPath jsonPath = new JsonPath(response.asInputStream());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(jsonPath.getString("msg")).isEqualTo("book is successfully deleted");
    }
}
