package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonDatabase {
    private static final String DB_PATH = "./src/main/java/server/data/db.json";
    private static final Path PATH = Path.of(DB_PATH);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final Gson gson = new Gson();


    public void set(Object key, JsonElement value) {
        writeLock.lock();

        try {
            JsonObject database = readDbContent();
            String[] keysArray = createStringArray(key);
            setValue(value, database, keysArray);
            Files.writeString(PATH, gson.toJson(database));
        } catch (IOException e) {
            System.err.println("Error while setting new value");
        } finally {
            writeLock.unlock();
        }
    }

    public Object get(Object key) {
        readLock.lock();

        Object value = null;
        try {
            JsonObject database = readDbContent();
            String[] keysArray = createStringArray(key);
            value = getValue(database, keysArray);
            return value;
        } catch (IOException e) {
            System.err.println("Error while getting value");
        } finally {
            readLock.unlock();
        }

        return value;
    }

    public boolean delete(Object key) {
        writeLock.lock();

        JsonElement deletedValue = null;
        try {
            JsonObject database = readDbContent();
            String[] keysArray = createStringArray(key);
            deletedValue = deleteValue(database, keysArray);
            Files.writeString(PATH, gson.toJson(database));
        } catch (IOException e) {
            System.err.println("Error while deleting value");
        } finally {
            writeLock.unlock();
        }

        return deletedValue != null;
    }

    private JsonObject readDbContent() throws IOException {
        String dbString = new String(Files.readAllBytes(PATH));

        return gson.fromJson(dbString, JsonObject.class);
    }

    private String[] createStringArray(Object key) {
        String[] keys;

        if (key instanceof String) {
            keys = new String[]{key.toString()};
            System.out.println("string");
        } else {
            keys = new Gson().fromJson(key.toString(), String[].class);
            System.out.println("array");
        }

        return keys;
    }

    private void setValue(JsonElement value, JsonObject jsonObject, String[] keys) {
        String currentKey = keys[0];

        if (keys.length == 1 && jsonObject.has(currentKey)) {
            jsonObject.add(currentKey, value);
            return;
        } else if (!jsonObject.has(currentKey)) {
            if (keys.length == 1) {
                jsonObject.add(currentKey, value);
                return;
            } else {
                jsonObject.add(currentKey, new JsonObject());
            }
        }

        JsonObject nestedObjectVal = jsonObject.getAsJsonObject(currentKey);
        String[] remainingKeys = Arrays.copyOfRange(keys, 1, keys.length);
        setValue(value, nestedObjectVal, remainingKeys);
    }

    private Object getValue(JsonObject jsonObject, String[] keys) {
        String currentKey = keys[0];

        if (keys.length == 1 && jsonObject.has(currentKey)) {
            return jsonObject.get(currentKey);
        } else if (!jsonObject.has(currentKey)) {
            return null;
        }

        JsonObject nestedJsonObjectVal = jsonObject.getAsJsonObject(currentKey);
        String[] remainingKeys = Arrays.copyOfRange(keys, 1, keys.length);
        return getValue(nestedJsonObjectVal, remainingKeys);
    }

    private JsonElement deleteValue(JsonObject jsonObject, String[] keys) {
        String currentKey = keys[0];

        if (keys.length == 1 && jsonObject.has(currentKey)) {
            return jsonObject.remove(currentKey);
        } else if (!jsonObject.has(currentKey)) {
            return null;
        }

        JsonObject nestedJsonObjectVal = jsonObject.getAsJsonObject(currentKey);
        String[] remainingKeys = Arrays.copyOfRange(keys, 1, keys.length);
        return deleteValue(nestedJsonObjectVal, remainingKeys);
    }
}