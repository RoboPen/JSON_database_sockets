package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Menu {
    private static final String DB_PATH = "./src/main/java/server/data/db.json";
    private static final Path PATH = Path.of(DB_PATH);
    private final MyClientSocket client;

    public Menu(MyClientSocket client) {
        this.client = client;
    }

    public void run(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while(true) {
                System.out.println("Enter !help for more info\n" +
                                   "Enter !print for printing db.json\n" +
                                   "Enter your request: ");

                String input = reader.readLine();
                if (input.equals("!help")) {
                    printHelpInfo();
                } else if(input.equals("!print")){
                    printDb();
                }
                else {
                    client.run(getArguments(input));
                }

                if(input.equals("-t exit")){
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Incorrect value of parameter provided");
            run();
        } catch (ParameterException e){
            System.err.println("Incorrect parameter provided: " + e.getMessage());
            run();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            run();
        }
    }

    private void printDb() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String dbContent = "";
        try {
             dbContent = new String(Files.readAllBytes(PATH));
        } catch (IOException e){
            System.err.println("Error while reading db.json. Content cannot be printed");
        }

        JsonElement je = JsonParser.parseString(dbContent);
        System.out.println("-------------------------------\n" +
                           gson.toJson(je) + "\n" +
                           "-------------------------------\n");
    }

    private Arguments getArguments(String input){
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(input.split(" "));
        return arguments;
    }

    private void printHelpInfo(){
        System.out.println("-------------------------------\n" +
              "get value: -t get -k key\n" +
              "set value: -t set -k key -v value\n" +
              "delete key: -t delete -k\n" +
              "pass the JSON file: -in fileName.json\n" +
              "shut down the server: -t exit\n" +
              "-------------------------------\n");
    }
}
