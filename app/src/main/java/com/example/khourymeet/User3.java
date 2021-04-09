package com.example.khourymeet;

import java.util.List;

public class User3 {

    private Boolean align;
    private String email;
    private String image;
    private String firstSemester;
    private String name;
    private String password;
    private String username;
    private List<Long> courses;
    private List<String> friends;

    //TODO: understand issue -- if courses are 5001, they are saved as Longs; if CS5001,
    // saved as Strings

    public User3() {
    }

    public Boolean getAlign() {
        return align;
    }

    public void setAlign(Boolean align) {
        this.align = align;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstSemester() {
        return firstSemester;
    }

    public void setFirstSemester(String firstSemester) {
        this.firstSemester = firstSemester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Long> getCourses() {
        return courses;
    }

    public void setCourses(List<Long> courses) {
        this.courses = courses;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

}
