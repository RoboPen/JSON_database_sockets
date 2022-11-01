package client;

import com.google.gson.Gson;

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

        Arguments request;
        if (inputFile == null) {
            request = createRequestFromArgs(arguments);
        } else {
            request = createRequestFromFile(inputFile);
        }

        return gson.toJson(request);
    }

    private Arguments createRequestFromArgs(Arguments arguments) {
        String type = arguments.getType();
        String key = arguments.getKey();
        String value = arguments.getValue();

        Arguments request = new Arguments();
        request.setType(type);
        if (key != null) {
            request.setKey(key);
        }
        if (value != null) {
            request.setValue(value);
        }
        return request;
    }

    private Arguments createRequestFromFile(String inputFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(Path.of(INPUT_FILE_DIR + inputFile)));
        System.out.println(fileContent);
        return gson.fromJson(fileContent, Arguments.class);
    }
}
