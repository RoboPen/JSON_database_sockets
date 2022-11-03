package server;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyServerSocket {
    private final ResponseHandler responseHandler;
    private final String address;
    private final int port;

    public MyServerSocket(ResponseHandler requestHandler, String address, int port) {
        this.responseHandler = requestHandler;
        this.address = address;
        this.port = port;
    }

    public void run() throws IOException {

        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        System.out.println("Server started!");
        ExecutorService executor = Executors.newFixedThreadPool(4);
        AtomicBoolean running = new AtomicBoolean(true);

        while (running.get()) {
            //client.Main.main(new String[2]);
            Socket socket = server.accept(); // accepting a new client

            executor.submit(() -> {
                try (
                        socket;
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String command = input.readUTF(); // reading a message
                    System.out.println("Received: " + command);

                    JSONObject request = new JSONObject(command);
                    String jsonResponse = responseHandler.getJsonResponse(request);

                    output.writeUTF(jsonResponse); // resend it to the client
                    System.out.println("Sent: " + jsonResponse);

                    if ("exit".equals(request.getString("type"))) {
                        running.set(false);
                        System.exit(0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}


