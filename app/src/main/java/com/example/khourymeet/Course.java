package com.example.khourymeet;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String currentStudents;
    private String mentors;

    public Course() {
    }

    public String getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(String currentStudents) {
        this.currentStudents = currentStudents;
    }

    public String getMentors() {
        return mentors;
    }

    public void setMentors(String mentors) {
        this.mentors = mentors;
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
}
