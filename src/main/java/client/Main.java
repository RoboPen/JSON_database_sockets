package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) throws IOException {
        //Arguments arguments = new Arguments();
        /*arguments.setType("set");
        arguments.setKey("1");
        arguments.setValue("Hello World!");*/
        //arguments.setInputFile("test2.json");
        List<String[]> listOfargs = new ArrayList<>();
        /*listOfargs.add("-t set -k 1 -v testowo".split(" "));
        listOfargs.add("-t get -k 1".split(" "));
        listOfargs.add("-t delete -k 1".split(" "));
        listOfargs.add("-t exit".split(" "));*/
        listOfargs.add("-in test1.json".split(" "));

        for(String[] array : listOfargs) {
            Arguments arguments = new Arguments();
                JCommander.newBuilder()
                        .addObject(arguments)
                        .build()
                        .parse(array);

                RequestHandler requestHandler = new RequestHandler(new Gson());
                MyClientSocket client = new MyClientSocket(requestHandler, ADDRESS, PORT);
                client.run(arguments);
        }

    }

    public static void runForest() throws IOException {
        List<String[]> listOfargs = new ArrayList<>();
        listOfargs.add("-in test2.json".split(" "));

        for(String[] array : listOfargs) {
            Arguments arguments = new Arguments();
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse(array);

            RequestHandler requestHandler = new RequestHandler(new Gson());
            MyClientSocket client = new MyClientSocket(requestHandler, ADDRESS, PORT);
            client.run(arguments);
        }
    }
}
