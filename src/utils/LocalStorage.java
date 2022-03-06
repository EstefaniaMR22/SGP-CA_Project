package utils;

import java.util.HashMap;

public class LocalStorage {
    private static final HashMap<String, Object> storage = new HashMap<>();

    public static Object get(String key) {
        return storage.get(key);
    }

    public static void set(String key, Object value) {
        storage.put(key, value);
    }



}
