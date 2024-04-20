package com.example.finalproject.model;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String email;
    private String name;
    private String password;
    private String number;
    private int age;
    private UserRole role;

    public User(String email, String name, String password, String number, String age, String role) throws Exception {
        if (email.equals("")) throw new Exception("Email cannot be empty");
        if (name.equals("")) throw new Exception("Name cannot be empty");
        if (password.equals("")) throw new Exception("Password cannot be empty");
        if (number.equals("") || number.length() < 10) throw new Exception("A phone number should be at least 10 digits long");
        if (age.equals("")) throw new Exception("Age cannot be empty");
        if (role.equals("")) throw new Exception("Role cannot be empty");

        this.email = email;
        this.name = name;
        this.password = password;
        this.number = number;
        this.age = Integer.parseInt(age);
        this.role= UserRole.valueOf(role);
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public User setRole(UserRole role) {
        this.role = role;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public User setNumber(String number) {
        this.number = number;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public HashMap<String,Object> toHashMap(){
        HashMap<String,Object> result= new HashMap<>();
        result.put("email",this.email);
        result.put("password",this.password);
        result.put("name",this.name);
        result.put("age",this.age);
        result.put("phoneNumber",this.number);
        result.put("role",this.role);
        result.put("Lessons", new ArrayList<String>());
        if(role==UserRole.TEACHER) {
            result.put("subjects", new ArrayList<String>());
            result.put("Sunday", "Not working");
            result.put("Monday", "Not working");
            result.put("Tuesday", "Not working");
            result.put("Wednesday", "Not working");
            result.put("Thursday", "Not working");
            result.put("Friday", "Not working");
            result.put("Saturday", "Not working");
            result.put("About me", " ");
            result.put("Cost", "0");
        }
        return result;
    }

    public String toJson() {
        Gson g = new Gson();
        return g.toJson(this);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", number='" + number + '\'' +
                ", age=" + age+ '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
