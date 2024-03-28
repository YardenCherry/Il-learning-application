package com.example.finalproject.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.finalproject.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashSet;
import java.util.Map;


public class LocalStorageService {
    private static LocalStorageService instance = null;
    private static final String DB_FILE = "com.example.finalproject";
    private SharedPreferences sharedPref;
    private static final String USER_KEY = "user";

//    public void putString(String key, String value) {
//        sharedPref.edit().putString(key,value).apply();
//    }

    public static Map<String, Object> getUser(){
        String userJson = instance.sharedPref.getString(USER_KEY, "NONE");
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

    public static void updateUser(String id){
        String userId = id.equals("") ? getUser().get("id").toString() : id;

        FireStoreService.findUser(userId)
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> userMap = documentSnapshot.getData();
                    userMap.put("id", documentSnapshot.getId());
                    instance.saveUser(userMap);
                })
                .addOnFailureListener(error -> {});
    }

}
