package com.example.finalproject.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;


public class LocalStorageService {
    private static LocalStorageService instance = null;
    private static final String DB_FILE = "com.example.finalproject";
    private SharedPreferences sharedPref;
    private static final String USER_KEY = "user";

    public static Map<String, Object> getUser(){
        String userJson = instance.sharedPref.getString(USER_KEY, "NONE");
        if(userJson.equals("NONE")) return null;
        Gson j = new Gson();
        Map<String, Object> userMap = j.fromJson(userJson, new TypeToken<Map<String, Object>>(){}.getType());
        return userMap;
    }

    public static void saveUser(Map<String, Object> userMap) {
        Gson g = new Gson();
        String mapJson = g.toJson(userMap);
        instance.sharedPref.edit().putString(USER_KEY, mapJson).apply();
    }

    public static void initService(Context context){
        instance = new LocalStorageService(context);
    }


    public static LocalStorageService getInstance(Context context) {
        if(instance == null)
            instance= new LocalStorageService(context);
        return instance;
    }


    private LocalStorageService(Context context) {
        sharedPref = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void updateUser(DocumentSnapshot userDoc){
        String userId = userDoc.getId();

        Map<String, Object> userMap = userDoc.getData();
        userMap.put("id", userId);
        saveUser(userMap);
    }
    public static void userLogOut(){
        instance.sharedPref.edit().remove(USER_KEY).apply();
    }

}
