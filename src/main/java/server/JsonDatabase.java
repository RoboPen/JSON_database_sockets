package server;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase {
    private static final String DB_PATH = "src/main/java/server/data/db.json";
    //    private static final String DB_PATH = System.getProperty("user.dir") + "/JSON Database/task/src/server/data/db.json";
    private static final Path PATH = Path.of(DB_PATH);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void set(JSONObject request) throws IOException {
        writeLock.lock();

        JSONObject database = readDbContent();
        JSONArray keys =  request.getJSONArray("key");
        String[] keysArray = keys.toList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
        database = setValue(request.get("value"), database, keysArray);
        Files.writeString(PATH, database.toString());

        writeLock.unlock();
    }

    public String get(JSONObject request) throws IOException {
        readLock.lock();

        JSONObject database = readDbContent();
        JSONArray keys =  request.getJSONArray("key");
        String[] keysArray = keys.toList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
        String value = getValue(database, keysArray);

        readLock.unlock();
        return value;
    }

    public boolean delete(JSONObject request) throws IOException {
        writeLock.lock();

        JSONObject database = readDbContent();
        JSONArray keys =  request.getJSONArray("key");
        String[] keysArray = keys.toList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
        JSONObject dbAfterDelOperation = removeValue(database, keysArray);
        database = dbAfterDelOperation == null ? database : dbAfterDelOperation;
        Files.writeString(PATH, database.toString());

        writeLock.unlock();
        return dbAfterDelOperation != null;
    }

    private JSONObject readDbContent() throws IOException {
        String dbString = new String(Files.readAllBytes(PATH));

        return new JSONObject(dbString);
    }

    private JSONObject setValue(Object value, JSONObject jsonObject, String[] keys)  {
        String currentKey = keys[0];

        if (keys.length == 1) {
            return jsonObject.put(currentKey, value);
        } else if (!jsonObject.has(currentKey)) {
            jsonObject.put(currentKey, new JSONObject());
        }

        JSONObject nestedJsonObjectVal = jsonObject.getJSONObject(currentKey);
        String[] remainingKeys = Arrays.copyOfRange(keys, 1, keys.length);
        JSONObject updatedNestedValue = setValue(value, nestedJsonObjectVal, remainingKeys);
        return jsonObject.put(currentKey, updatedNestedValue);
    }

    private String getValue(JSONObject jsonObject, String[] keys) {
        String currentKey = keys[0];

        if (keys.length == 1 && jsonObject.has(currentKey)) {
            return jsonObject.getString(currentKey);
        } else if (!jsonObject.has(currentKey)) {
            return null;
        }

        JSONObject nestedJsonObjectVal = jsonObject.getJSONObject(currentKey);
        String[] remainingKeys = Arrays.copyOfRange(keys, 1, keys.length);
        return getValue(nestedJsonObjectVal, remainingKeys);
    }

    private JSONObject removeValue(JSONObject jsonObject, String[] keys) {
        String currentKey = keys[0];

        if (keys.length == 1 && jsonObject.has(currentKey)) {
            jsonObject.remove(currentKey);
            return jsonObject;
        } else if (!jsonObject.has(currentKey)) {
            return null;
        }

        JSONObject nestedJsonObjectVal = jsonObject.getJSONObject(currentKey);
        String[] remainingKeys = Arrays.copyOfRange(keys, 1, keys.length);
        JSONObject updatedNestedValue = removeValue(nestedJsonObjectVal, remainingKeys);
        return jsonObject.put(currentKey, updatedNestedValue);
    }
}

