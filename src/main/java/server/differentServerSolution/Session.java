package server.differentServerSolution;

import org.json.JSONObject;
import server.Request;
import server.ResponseHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Session implements Runnable {
    private final ServerSocket server;
    private final Socket socket;
    private final ResponseHandler responseHandler;

    public Session(ServerSocket server, Socket socketForClient, ResponseHandler responseHandler) {
        this.server = server;
        this.socket = socketForClient;
        this.responseHandler = responseHandler;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String command = input.readUTF(); // reading a message
            System.out.println("Received: " + command);

            JSONObject request = new JSONObject(command);
            String jsonResponse = responseHandler.getJsonResponse(request);

            output.writeUTF(jsonResponse); // resend it to the client
            System.out.println("Sent: " + jsonResponse);

            if (request.getString("type").equals("exit")) {
                //server.close();
                //System.exit(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
