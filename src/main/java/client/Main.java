package client;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler();
        MyClientSocket client = new MyClientSocket(requestHandler, ADDRESS, PORT);
        Menu menu = new Menu(client);
        menu.run();
    }
}