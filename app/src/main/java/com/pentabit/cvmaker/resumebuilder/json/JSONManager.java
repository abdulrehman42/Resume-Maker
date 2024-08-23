package com.pentabit.cvmaker.resumebuilder.json;

import com.google.errorprone.annotations.Keep;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;

@Keep
public class JSONManager {

    private static JSONManager instance = null;

    private JSONManager() {
    }

    public static JSONManager getInstance() {
        if (instance == null) {
            instance = new JSONManager();
        }
        return instance;
    }

    JsonElement emptyJsonObject = new JsonObject();

    private JsonElement getJsonObject(JSONKeys key, JsonElement jsonElement) {
        if (jsonElement != null && jsonElement != emptyJsonObject && jsonElement.isJsonObject()) {
            JsonElement element = jsonElement.getAsJsonObject().get(key.getKey());
            if (element != null) {
                return element;
            }
            Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String key1 = entry.getKey();
                if (key1.equals(key.getKey())) {
                    return entry.getValue();
                }
                JsonElement jsonObject = getJsonObject(key, entry.getValue());
                if (jsonObject != null && jsonObject != emptyJsonObject) {
                    return jsonObject;
                }
            }
        }
        return emptyJsonObject;
    }

    private JsonElement getJsonObject(String key, JsonElement jsonElement) {
        if (jsonElement != null && jsonElement != emptyJsonObject && jsonElement.isJsonObject()) {
            JsonElement element = jsonElement.getAsJsonObject().get(key);
            if (element != null) {
                return element;
            }
            Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String key1 = entry.getKey();
                if (key1.equals(key)) {
                    return entry.getValue();
                }
                JsonElement jsonObject = getJsonObject(key, entry.getValue());
                if (jsonObject != null && jsonObject != emptyJsonObject) {
                    return jsonObject;
                }
            }
        }
        return emptyJsonObject;
    }

    public Object getFormattedResponse(JSONKeys key, JsonElement jsonElement, Type type) throws JsonSyntaxException {
        JsonElement rootJson = getJsonObject(key, jsonElement);
        return new Gson().fromJson(rootJson, type);
    }

    public Object getFormattedResponse(JSONKeys key, ResponseBody jsonElement, Type type) throws IOException {
        JsonElement rootJson = getJsonObject(key, ((JsonElement) convertJsonToObject(jsonElement.string(), new TypeToken<JsonElement>() {
        }.getType())));
        Gson gson = new Gson();
        return gson.fromJson(rootJson, type);
    }

    public Object getFormattedResponse(String key, JsonElement jsonElement, Type type) {
        JsonElement rootJson = getJsonObject(key, jsonElement);
        Gson gson = new Gson();
        return gson.fromJson(rootJson, type);
    }

    public Object getFormattedResponse( JsonElement jsonElement, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(jsonElement, type);
    }

    public Object convertJsonToObject(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
