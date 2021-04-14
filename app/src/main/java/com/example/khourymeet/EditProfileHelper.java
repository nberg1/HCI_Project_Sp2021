package com.example.khourymeet;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class EditProfileHelper {

    // Store username
    private String username;

    // Create list of current courses
    private String currentCoursesStr;
    private List<String> currentCourses;

    // Create list of past courses
    private String pastCoursesStr;
    private List<String> pastCourses;

    // Create list of courses added
    private List<String> coursesAdded;
    // Create list of courses completed
    private List<String> coursesCompleted;
    // Create list of courses withdrawn
    private List<String> coursesWithdrawn;
    // Create list of courses deleted
    private List<String> coursesDeleted;

    public EditProfileHelper(String inUsername) {
        this.username = inUsername;
        currentCourses = new ArrayList<>();
        pastCourses = new ArrayList<>();
        coursesAdded = new ArrayList<>();
        coursesCompleted = new ArrayList<>();
        coursesWithdrawn= new ArrayList<>();
        coursesDeleted = new ArrayList<>();
    }

}
