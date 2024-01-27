package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class PayloadUtil {

    public static String addBookBody(Map<String, String> bookData) {
        String book = "{\n" +
                "    \"name\": \"" + bookData.get("name") + "\",\n" +
                "    \"isbn\": \"" + bookData.get("isbn") + "\",\n" +
                "    \"aisle\": \"" + bookData.get("aisle") + "\",\n" +
                "    \"author\": \"" + bookData.get("author") + "\"\n" +
                "}";
        return book;
    }

    public static String addPlaceBody() {
        return "{\n" +
                "  \"location\": {\n" +
                "    \"lat\": -38.383494,\n" +
                "    \"lng\": 33.427362\n" +
                "  },\n" +
                "  \"accuracy\": 50,\n" +
                "  \"name\": \"Frontline house\",\n" +
                "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                "  \"address\": \"29, side layout, cohen 09\",\n" +
                "  \"types\": [\n" +
                "    \"shoe park\",\n" +
                "    \"shop\"\n" +
                "  ],\n" +
                "  \"website\": \"http://google.com\",\n" +
                "  \"language\": \"French-IN\"\n" +
                "}";
    }

    public static String updatePlaceBody(String placeId) {
        return "{\n" +
                "    \"place_id\": \"" + placeId + "\",\n" +
                "    \"address\": \"Hyderabad, India\",\n" +
                "    \"key\": \"qaclick123\"\n" +
                "}";
    }

    public static String deletePlaceBody(String placeId) {
        return "{\n" +
                "    \"place_id\": \"" + placeId + "\"\n" +
                "}";
    }

    public static String demoJsonArray() {
        try {
            InputStream inputStream = PayloadUtil.class.getClassLoader().getResourceAsStream("json.models/jsonpathdemo.json");
            String json = new BufferedReader(
                    new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{ \"msg\" : \"No Json\"}";
    }

    public static String readJsonFile(String filePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            String jsonString = new String(bytes);
            return jsonString;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "File not found";
        }
    }
}
