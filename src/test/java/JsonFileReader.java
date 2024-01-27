import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class JsonFileReader {
    public static void main(String[] args) throws IOException {
        //Converts file contents to bytes
        byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/json.models/jsonpathdemo.json"));
        String jsonString = new String(bytes);
        System.out.println(Arrays.toString(bytes));
        System.out.println(jsonString); // will print the contents of jsonpathdemo.json file
    }
}
