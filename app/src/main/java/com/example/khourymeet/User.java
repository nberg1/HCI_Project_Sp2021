package com.example.khourymeet;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private String email;
    private String userName;
    private String password;
    private Boolean align;
    private String firstSemester;
    private String token;
    private List<String> friends; // direct chats
    private List<String> currentCourses;
    private List<String> pastCourses;
    private String image;

    public User() {
    }

    // TODO: is token necessary?
    public User(String name, String email, String userName, String password, String token) {
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.token = token;
        this.align = true;
        this.firstSemester = "";
        this.currentCourses = new ArrayList<>();
        this.pastCourses = new ArrayList<>();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Boolean getAlign() {
        return align;
    }

    public void setAlign(Boolean align) {
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

    public List<String> getCurrentCourses() {
        return currentCourses;
    }

    public void setCurrentCourses(List<String> currentCourses) {
        this.currentCourses = currentCourses;
    }

    public List<String> getPastCourses() {
        return pastCourses;
    }

    public void setPastCourses(List<String> pastCourses) {
        this.pastCourses = pastCourses;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
