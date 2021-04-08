package com.example.khourymeet;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private String email;
    private String username;
    private String password;
    private Boolean align;
    private String firstSemester;
    private String token;
    private List<String> friends;
    private List<String> courses;
    private String imageFileName;

    public User() {
    }

    public User(String name, String email, String username, String password, String token) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.token = token;
        this.align = true;
        this.firstSemester = "";
        this.courses = new ArrayList<>();
        this.friends = new ArrayList<>();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
