package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TestEditProfile extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    private String currentUsername;
    private final String defaultString = "default";

    // TextViews for immutable user information
    private TextView usernameView;
    private TextView emailView;

    // TextViews for current and past courses
    private TextView currentCourse1View;
    private TextView currentCourse2View;

    // EditTextViews for changeable user information
    private EditText editName;
    private EditText editPassword;

    // Buttons for current courses
    private Button withdrawButton1;
    private Button completeButton1;
    private Button withdrawButton2;
    private Button completeButton2;

    // Create list of current courses
    private String currentCoursesStr;
    private List<String> currentCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_edit_profile);

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);
        currentUsername = "marielleTest";

        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentCourses = new ArrayList<>();

        getButtonViews();

        getTextViews();

        createUserSetText();

        // Complete Course1
        completeButton1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse1 = findViewById(R.id.user_currCourse1);
                        String course = currCourse1.getText().toString();
                        completeCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
        // Complete Course2
        completeButton2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse2 = findViewById(R.id.user_currCourse2);
                        String course = currCourse2.getText().toString();
                        completeCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
    }

    private void getTextViews() {
        // Get EditText views for profile texts
        editName = findViewById(R.id.name);
        editPassword = findViewById(R.id.password);

        // Get TextViews for profile texts
        usernameView = findViewById(R.id.username);
        emailView = findViewById(R.id.user_email);

        // Get TextViews for current courses
        currentCourse1View = findViewById(R.id.user_currCourse1);
        currentCourse2View = findViewById(R.id.user_currCourse2);
    }

    private void getButtonViews() {
        withdrawButton1 = findViewById(R.id.withdraw_btn1);
        withdrawButton2 = findViewById(R.id.withdraw_btn2);
        completeButton1 = findViewById(R.id.complete_btn1);
        completeButton2 = findViewById(R.id.complete_btn2);
    }

    // Create User object from Database entry for the current username and set the edit profile text
    private void createUserSetText() {
        databaseReference.child(getString(R.string.users_path,
                currentUsername)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create User object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User currentUser = snapshot.getValue(User.class);
                    setEditProfileText(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEditProfileText(User user) {
//        // Get EditText views for profile texts
//        editName = getView().findViewById(R.id.name);
//        editPassword = getView().findViewById(R.id.password);
//
//        // Get TextViews for profile texts
//        usernameView = getView().findViewById(R.id.username);
//        emailView = getView().findViewById(R.id.user_email);
//
//        // Get TextViews for current courses
//        currentCourse1View = getView().findViewById(R.id.user_currCourse1);
//        currentCourse2View = getView().findViewById(R.id.user_currCourse2);

        // Set texts for profile
        editName.setText(user.getName());
        editPassword.setText(user.getPassword());
        usernameView.setText(user.getUserName());
        emailView.setText(user.getEmail());

        // Create list of current courses
//        currentCourses = createCoursesList(user, 1);
        currentCoursesStr = user.getCurrentCourseList();
        currentCourses = user.convertCourseStrToArray(currentCoursesStr);

        // Populate current course views
        setCurrentCoursesViews();

//        programView.setText(createProgramString(user));
//        currentCoursesView.setText(createCoursesString(user, 1));
//        pastCoursesView.setText(createCoursesString(user, -1));

    }

    // Update courses upon course completion and pass to db
    private void completeCourse(String course) {
        currentCourses.remove(course);
        currentCoursesStr = currentCourses.toString();
        currentCoursesStr = currentCoursesStr.replaceAll("\\[", "");
        currentCoursesStr = currentCoursesStr.replaceAll("\\]", "");
        databaseReference.child("users").child(currentUsername).child("currentCourseList").setValue(currentCoursesStr);
        setCurrentCoursesViews();
//        DatabaseReference childRemoveVal = databaseReference.child(getString(R.string.users_path,
//                currentUsername)).child("currentCourses").child(course);
//        Log.w("create user", childRemoveVal.toString());
//        childRemoveVal.removeValue();
//        databaseReference.child(getString(R.string.users_path,
//                currentUsername)).addListenerForSingleValueEvent(new ValueEventListener() {
//            // Use snapshot to create User object
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//
//                    User currentUser = snapshot.getValue(User.class);
////                    setEditProfileText(currentUser);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    // Get string of courses
    // To get string of past courses, pass -1 for courseType
    // To get string of current courses, pass 1 for courseType
    private List<String> createCoursesList(User user, int courseType) {
        List<String> courses;
        if (courseType == -1) {
            courses = user.getPastCourses();
        } else {
            courses = user.getCurrentCourses();
        }
        return courses;
    }

    private void setCurrentCoursesViews() {
        if (currentCourses.size() < 1) {
            currentCourse1View.setText("NONE");
            currentCourse1View.setVisibility(View.GONE);
            withdrawButton1.setVisibility(View.GONE);
            completeButton1.setVisibility(View.GONE);
            currentCourse2View.setText("NONE");
            currentCourse2View.setVisibility(View.GONE);
            withdrawButton2.setVisibility(View.GONE);
            completeButton2.setVisibility(View.GONE);
        } else if (currentCourses.size() == 1) {
            currentCourse1View.setText(currentCourses.get(0));
            currentCourse2View.setText("NONE");
            currentCourse2View.setVisibility(View.GONE);
            withdrawButton2.setVisibility(View.GONE);
            completeButton2.setVisibility(View.GONE);
        } else {
            currentCourse1View.setText(currentCourses.get(0));
            currentCourse2View.setText(currentCourses.get(1));
        }
    }
}