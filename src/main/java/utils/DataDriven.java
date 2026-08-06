package utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileReader;

public class DataDriven {

    private static final String FILE_PATH = System.getProperty("user.dir") + File.separator
            + "src" + File.separator + "testData" + File.separator + "testData.json";

    public static String jsonReader(String objectName, String keyName) {
        String filePath = FILE_PATH;
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject targetObject = (JSONObject) jsonObject.get(objectName);

            if (targetObject == null) {
                System.err.println("Object not found in JSON: " + objectName);
                return null;
            }

            return (String) targetObject.get(keyName);

        } catch (Exception e) {
            System.err.println("Failed to read file at path: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

    // New: reads a top-level JSON array by key, e.g. "cartProducts": [...]
    public static JSONArray jsonArrayReader(String arrayName) {
        String filePath = FILE_PATH;
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray array = (JSONArray) jsonObject.get(arrayName);

            if (array == null) {
                System.err.println("Array not found in JSON: " + arrayName);
                return new JSONArray();
            }

            return array;

        } catch (Exception e) {
            System.err.println("Failed to read file at path: " + filePath);
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
