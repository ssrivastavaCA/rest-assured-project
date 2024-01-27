import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import util.PayloadUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonValidationsTest {

    JsonPath jsonPath = new JsonPath(PayloadUtil.demoJsonArray());

    @Test
    public void readJsonArrays() {
        int booksSize = jsonPath.get("book.size()");
        float cheapPrice = jsonPath.get("priceRange.cheap");
        float mediumPrice = jsonPath.get("priceRange.medium");
        // uses a Groovy shell to evaluate expressions so be careful when injecting user input into the expression
        // https://www.javadoc.io/doc/com.jayway.restassured/json-path/2.8.0/com/jayway/restassured/path/json/JsonPath.html
        // https://www.baeldung.com/guide-to-jayway-jsonpath
        String authorOfJsonBook = jsonPath.get("book.find{book -> book.title == 'JsonBook'}.author");
        List<String> listOfAuthors = jsonPath.get("book.author");
        float priceOf1stBook = jsonPath.get("book[0].price");

        System.out.println(authorOfJsonBook);
        System.out.println(listOfAuthors);
        System.out.println(priceOf1stBook);

        assertThat(booksSize).isEqualTo(4);
        assertThat(cheapPrice).isEqualTo(10.0f);
        assertThat(mediumPrice).isEqualTo(20.0f);
        assertThat(authorOfJsonBook).isEqualTo("Ben Smith");
    }
}
