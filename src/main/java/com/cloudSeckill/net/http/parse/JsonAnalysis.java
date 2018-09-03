package com.cloudSeckill.net.http.parse;

import com.cloudSeckill.net.http.callback.ResultEntity;
import com.cloudSeckill.net.http.callback.ResultEntity2;
import com.google.gson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class JsonAnalysis {
    public static final String RESULT_STATUS = "status";
    public static final String RESULT_ROWS = "rows";
    public static final String RESULT_OTHER = "other";
    public static final String RESULT_TOTAL = "total";
    public static final String RESULT_MSG = "msg";
    
    public static final String RESULT_DATA = "data";
    
    private static Gson gson;
    private static final JsonParser jsonParser = new JsonParser();

    static {
        gson = new GsonBuilder()
//                .registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
//                    @Override
//                    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                        return new Timestamp(getServerDate(json));
//                    }
//                })
//                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                    @Override
//                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                        return getServerDate(json);
//                    }
//                })
                .registerTypeAdapter(List.class, new JsonDeserializer<List>() {
                    @Override
                    public List deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        if (json.isJsonArray()) {
                            JsonArray array = json.getAsJsonArray();
                            Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                            List list = new ArrayList();
                            for (int i = 0; i < array.size(); i++) {
                                JsonElement element = array.get(i);
                                Object item = context.deserialize(element, itemType);
                                list.add(item);
                            }
                            return list;
                        } else {
                            return Collections.EMPTY_LIST;
                        }
                    }
                })
                .create();
    }


    public static String toJson(Object object) {
        String jsonString = null;
        if (gson != null) {
            jsonString = gson.toJson(object);
        }
        return jsonString;
    }

    public static String getJsonFilterElement(String result, String key) {
        JsonElement jsonElement = jsonParser.parse(result);
        try {
            Set<Map.Entry<String, JsonElement>> setJsons = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> set : setJsons) {
                String name = set.getKey();
                if (name.equals(key)) {
                    return set.getValue().toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultEntity parseResultEntity(String json){
        return gson.fromJson(json,ResultEntity.class);
    }
    public static ResultEntity2 parseResultEntity2(String json){
        return gson.fromJson(json,ResultEntity2.class);
    }
}
