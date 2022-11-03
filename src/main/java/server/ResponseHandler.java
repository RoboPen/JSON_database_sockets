package server;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.IOException;

public class ResponseHandler {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";

    private final JsonDatabase jsonDatabase;
    private final Gson gson = new Gson();

    public ResponseHandler(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public String getJsonResponse(JSONObject request) throws IOException {
        Response response = new Response();
        switch (request.getString("type")) {
            case "exit":
                response.setResponse(OK);
                break;
            case "set":
                executeSetCommand(request, response);
                break;
            case "get":
                executeGetCommand(request, response);
                break;
            case "delete":
                executeDeleteCommand(request, response);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Command %s is not supported", request.getString("type"))
                );

        }
        return gson.toJson(response);
    }

    private void executeSetCommand(JSONObject request, Response response) throws IOException {
        jsonDatabase.set(request);
        response.setResponse(OK);
    }

    private void executeGetCommand(JSONObject request, Response response) throws IOException {
        String value = jsonDatabase.get(request);
        if (value != null) {
            response.setResponse(OK);
            response.setValue(value);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }

    private void executeDeleteCommand(JSONObject request, Response response) throws IOException {
        if (jsonDatabase.delete(request)) {
            response.setResponse(OK);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }
}
