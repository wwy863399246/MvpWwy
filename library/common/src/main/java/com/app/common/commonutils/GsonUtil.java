package com.app.common.commonutils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtil
{

    private static Gson _gson;

    public static Gson defaultGson()
    {
        if (_gson == null)
        {
            _gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss")
//                    .registerTypeAdapter(Date.class,new DateSerizlier())
                    .create();
        }
        return _gson;
    }

    public static <T> T parseGson(String jsonStr, Type clas)
    {
        try
        {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(jsonStr, clas);
        }
        catch (Exception e)
        {
            Log.d("Exception_GsonUtil", e.toString());
            return null;
        }

    }

    public static <T> List<T> jsonToBeanList(String json, Class<T> t)
    {
        Gson gson = new GsonBuilder().create();
        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(json).getAsJsonArray();
        for (JsonElement element : jsonarray)
        {
            list.add(gson.fromJson(element, t));
        }
        return list;
    }
}
