package com.legendwd.hyperpay.aelf.util;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {

    private static Gson create() {
        return JsonUtils.GsonHolder.gson;
    }

    private static class GsonHolder {
        private static Gson gson = new Gson();
    }

    //JSON字符串转对象,此方法不可用来转带泛型的集合
    public static <T> T jsonToObj(String jsonObjStr, Class<T> clazs) {
        if (TextUtils.isEmpty(jsonObjStr) || jsonObjStr.length() <= 2) {
            return null;
        }
        return create().fromJson(jsonObjStr, clazs);
    }

    //对象转JSON字符串
    public static String objToJson(Object object) {
        return create().toJson(object);
    }

    /**
     * JSON字符串转集合
     * 调用实例：
     * List<Student> convStudents=JsonUtils.jsonConvert(json, new TypeToken<List<Student>>(){}.getType());
     */
    public static <T> List<T> jsonToList(String JsonStr, Type typeOfT) {
        return create().fromJson(JsonStr, typeOfT);
    }

    //JSON字符串转集合
    public static <T> List<T> jsonToList(String jsonStr, Class<T> clasz) {
        if (!TextUtils.isEmpty(jsonStr)) {
            JsonElement jsonElement = new JsonParser().parse(jsonStr);
            if (jsonElement.isJsonNull()) {
                throw new RuntimeException("得到的jsonElement对象为空");
            }
            if (!jsonElement.isJsonArray()) {
                throw new RuntimeException("json字符不是一个数组对象集合");
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<T> beans = new ArrayList<>();
            for (JsonElement jsonElement2 : jsonArray) {
                T bean = new Gson().fromJson(jsonElement2, clasz);
                beans.add(bean);
            }
            return beans;
        }
        return null;
    }

    /**
     * JSON字符串转Map
     * 调用实例：
     */
    public static <T> Map<String, String> jsonToMap(String JsonStr, TypeToken<Map<String, String>> typeOfT) {
        return create().fromJson(JsonStr, typeOfT.getType());
    }

    public static <T> T jsonConvert(String json, Type type) throws Exception {
        return create().fromJson(json, type);
    }

    public static String getJsonValue(String key, String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.optString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getJsonValue(String key, JSONObject jsonObj) {
        return jsonObj.optString(key);
    }

    /**
     * 遍历json 手动塞入key,value
     *
     * @param params
     * @return
     */
    public static HashMap<String, Object> ergodicData(String params) {
        HashMap<String, Object> hashMap = new HashMap();
        Gson gson = create();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(params, type);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    public static int getJsonValueInt(String key, String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object.optInt(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getJsonObjString(Uri uri) {
        JSONObject object = new JSONObject();
        try {
            Set<String> names = uri.getQueryParameterNames();
            if (names != null && names.size() > 0) {
                for (String key : names) {
                    object.put(key, uri.getQueryParameter(key));
                }
            }
        } catch (Exception e) {
            Log.e("error", "getJsonObjString method  throw exception");
            return "";
        }
        return object.toString();
    }

}
