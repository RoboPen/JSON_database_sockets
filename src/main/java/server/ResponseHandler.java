package server;

import com.google.gson.Gson;

import java.io.IOException;

public class ResponseHandler {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";

    private final JsonDatabase jsonDatabase;
    private final Gson gson = new Gson();

    public ResponseHandler(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public String getJsonResponse(Request request) throws IOException {
        Response response = new Response();
        switch (request.getType()) {
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
                        String.format("Command %s is not supported", request.getType())
                );

        }
        return gson.toJson(response);
    }

    public Request getRequest(String command) {
        System.out.println(command);
        Request rec = gson.fromJson(command, Request.class);
        System.out.println(rec.toString());
        return gson.fromJson(command, Request.class);
    }

    private void executeSetCommand(Request request, Response response) throws IOException {
        jsonDatabase.set(request.getKey(), request.getValue());
        response.setResponse(OK);
    }

    private void executeGetCommand(Request request, Response response) throws IOException {
        String value = jsonDatabase.get(request.getKey());
        if (value != null) {
            response.setResponse(OK);
            response.setValue(value);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }

    private void executeDeleteCommand(Request request, Response response) throws IOException {
        if (jsonDatabase.delete(request.getKey())) {
            response.setResponse(OK);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }
}
