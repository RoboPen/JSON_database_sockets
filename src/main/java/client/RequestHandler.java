package client;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler {
    //    private static final String INPUT_FILE_DIR = System.getProperty("user.dir") + "/JSON Database/task/src/client/data/";
    private static final String INPUT_FILE_DIR = "src/main/java/client/data/";

    private final Gson gson;

    public RequestHandler(Gson gson) {
        this.gson = gson;
    }

    public String createRequest(Arguments arguments) throws IOException {
        String inputFile = arguments.getInputFile();

        JSONObject request;
        if (inputFile == null) {
            request = createRequestFromArgs(arguments);
        } else {
            request = createRequestFromFile(inputFile);
        }

        return request.toString();
    }

    private JSONObject createRequestFromArgs(Arguments arguments) {
        String type = arguments.getType();
        String key = arguments.getKey();
        String value = arguments.getValue();

        JSONObject request = new JSONObject();
        request.put("type", type);
        if (key != null) {
            request.put("key", key);
        }
        if (value != null) {
            request.put("value", value);
        }
        return request;
    }

    private JSONObject createRequestFromFile(String inputFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(Path.of(INPUT_FILE_DIR + inputFile)));

        return new JSONObject(fileContent);
    }
}
