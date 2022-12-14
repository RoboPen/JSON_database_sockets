package server;

import java.io.IOException;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) throws IOException {
        JsonDatabase jsonDatabase = new JsonDatabase();
        ResponseHandler responseHandler = new ResponseHandler(jsonDatabase);
        MyServerSocket server = new MyServerSocket(responseHandler, ADDRESS, PORT);
        server.run();
    }
}