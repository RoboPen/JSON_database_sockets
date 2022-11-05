package server.differentServerSolution;

import server.ResponseHandler;
import server.differentServerSolution.Session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServerSocket {
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    private final ResponseHandler responseHandler;
    private  final String address;
    private final int port;

    public MyServerSocket(ResponseHandler requestHandler, String address, int port) {
        this.responseHandler = requestHandler;
        this.address = address;
        this.port = port;
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            while (true) {
                Session session = new Session(server.accept(), responseHandler);
                executor.submit(session);
            }
        } catch (IOException e) {
            System.out.println("Server error");
        }
    }
}
