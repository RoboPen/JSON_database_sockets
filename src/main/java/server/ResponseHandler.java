package server;

import client.Request;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ResponseHandler {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private final Gson gson = new Gson();

    private final JsonDatabase jsonDatabase;


    public ResponseHandler(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public String getJsonResponse(Request request)  {
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
        return gson.fromJson(command, Request.class);
    }

    private void executeSetCommand(Request request, Response response)  {
        JsonElement value = gson.toJsonTree(request.getValue());
        jsonDatabase.set(request.getKey(), value);
        response.setResponse(OK);
    }

    private void executeGetCommand(Request request, Response response)  {
        Object value = jsonDatabase.get(request.getKey());
        if (value != null) {
            response.setResponse(OK);
            response.setValue(value);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }

    private void executeDeleteCommand(Request request, Response response) {
        if (jsonDatabase.delete(request.getKey())) {
            response.setResponse(OK);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }
}