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
    private String friends; // direct chats
    private String currentCourses;
    private String pastCourses;

    public User() {
    }

    // TODO: is token necessary?
    public User(String name, String email, String userName, String password) {
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.align = true;
        this.firstSemester = "";
        this.currentCourses = "";
        this.pastCourses = "";
        this.friends = "";
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

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getCurrentCourses() {
        return currentCourses;
    }

    public void setCurrentCourses(String currentCourses) {
        this.currentCourses = currentCourses;
    }

    public String getPastCourses() {
        return pastCourses;
    }

    public void setPastCourses(String pastCourses) {
        this.pastCourses = pastCourses;
    }

    // Convert string list to an array
    public List<String> convertStrToArray(String str) {
        List<String> arr = new ArrayList<>();
        if (str == null || str.equals("")) {
            return arr;
        } else {
            String[] tempStr = str.split(", ");
            for (String s : tempStr) {
                arr.add(s);
            }
        }
        return arr;
    }

    // After running toString on an array, remove the brackets from the String
    public String removeBracketsArrStr(String arrayString) {
        String tempStr = arrayString.replaceAll("\\[", "");
        tempStr = tempStr.replaceAll("\\]", "");
        return tempStr;
    }
}
