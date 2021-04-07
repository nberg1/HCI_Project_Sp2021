package com.example.khourymeet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User2 {

    private Boolean align;
//    private List<String> courses;
    private HashMap<String, String> courses;
    private String email;
    private String firstSemester;
//    private List<String> friends;
    private HashMap<String, String> friends;
    private String image;
    private String name;
    private String password;
    private String username;
//    private String token;

    public User2() {
    }

    public User2(String name, String email, String username, String password, HashMap<String, String> courses, HashMap<String, String> friends) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.align = true;
        this.firstSemester = "";
        this.courses = courses;
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }

    public Boolean getAlignInfo() {
        return align;
    }

    public void setAlignInfo(Boolean align) {
        this.align = align;
    }

    public String getFirstSemester() {
        return firstSemester;
    }

    public void setFirstSemester(String firstSemester) {
        this.firstSemester = firstSemester;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, String> friends) {
        this.friends = friends;
    }

    public HashMap<String, String> getCourses() {
        return courses;
    }

    public void setCourses(HashMap<String, String> courses) {
        this.courses = courses;
    }

    public String getImageFileName() {
        return image;
    }

    public void setImageFileName(String imageFileName) {
        this.image = imageFileName;
    }
}
