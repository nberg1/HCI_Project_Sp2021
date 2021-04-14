package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private TextView pastCourse1View;
    private TextView pastCourse2View;
    private TextView pastCourse3View;
    private TextView pastCourse4View;

    // EditTextViews for changeable user information
    private EditText editName;
    private EditText editPassword;

    // Button for add course
    private Button addCourseButton;
    
    // Button to save
    private Button saveButton;

    // Buttons for current courses
    private Button withdrawButton1;
    private Button completeButton1;
    private Button withdrawButton2;
    private Button completeButton2;

    // Buttons for past courses
    private Button deleteButton1;
    private Button deleteButton2;
    private Button deleteButton3;
    private Button deleteButton4;

    // Checkboxes for past courses
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;

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
        pastCourses = new ArrayList<>();
        coursesAdded = new ArrayList<>();
        coursesCompleted = new ArrayList<>();
        coursesWithdrawn= new ArrayList<>();
        coursesDeleted = new ArrayList<>();

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
        // Withdraw Course1
        withdrawButton1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse1 = findViewById(R.id.user_currCourse1);
                        String course = currCourse1.getText().toString();
                        withdrawCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
        // Withdraw Course2
        withdrawButton2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse2 = findViewById(R.id.user_currCourse2);
                        String course = currCourse2.getText().toString();
                        withdrawCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
        // Delete Past Course1
        deleteButton1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = findViewById(R.id.user_prevCourse1);
                        String course = pastCourse.getText().toString();
                        deleteCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
        // Delete Past Course2
        deleteButton2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = findViewById(R.id.user_prevCourse2);
                        String course = pastCourse.getText().toString();
                        deleteCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
        // Delete Past Course3
        deleteButton3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = findViewById(R.id.user_prevCourse3);
                        String course = pastCourse.getText().toString();
                        deleteCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );
        // Delete Past Course4
        deleteButton4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = findViewById(R.id.user_prevCourse4);
                        String course = pastCourse.getText().toString();
                        deleteCourse(course);
                        Log.w("tag", "updated course");
                    }
                }
        );


        // Save
        saveButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        // Send current and past courses to db
                        // Add/remove user from course currentStudents and mentors
                        Log.w("currentCourses", currentCoursesStr);
                        Log.w("pastCourses", pastCoursesStr);
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

        // Get TextViews for past courses
        pastCourse1View = findViewById(R.id.user_prevCourse1);
        pastCourse2View = findViewById(R.id.user_prevCourse2);
        pastCourse3View = findViewById(R.id.user_prevCourse3);
        pastCourse4View = findViewById(R.id.user_prevCourse4);
    }

    private void getButtonViews() {
        addCourseButton = findViewById(R.id.button4);
        saveButton = findViewById(R.id.save_button);

        withdrawButton1 = findViewById(R.id.withdraw_btn1);
        withdrawButton2 = findViewById(R.id.withdraw_btn2);

        completeButton1 = findViewById(R.id.complete_btn1);
        completeButton2 = findViewById(R.id.complete_btn2);

        deleteButton1 = findViewById(R.id.delete_btn1);
        deleteButton2 = findViewById(R.id.delete_btn2);
        deleteButton3 = findViewById(R.id.delete_btn3);
        deleteButton4 = findViewById(R.id.delete_btn4);

        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
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
                    if (currentUser != null) {
                        setEditProfileText(currentUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEditProfileText(User user) {
        // Set texts for profile
        editName.setText(user.getName());
        editPassword.setText(user.getPassword());
        usernameView.setText(user.getUserName());
        emailView.setText(user.getEmail());

        // Create list of current courses
        currentCoursesStr = user.getCurrentCourses();
        currentCourses = user.convertStrToArray(currentCoursesStr);

        // Create list of past courses
        pastCoursesStr = user.getPastCourses();
        pastCourses = user.convertStrToArray(pastCoursesStr);

        // Populate current course views
        setCurrentCoursesViews();

        // Populate past course views
        setPastCoursesViews();

    }

    // Update courses upon course completion
    private void completeCourse(String course) {
        currentCourses.remove(course); // List
        pastCourses.add(course); // List
        coursesCompleted.add(course);
        setCurrentCoursesViews();
        setPastCoursesViews();
        currentCoursesStr = removeBracketsArrStr(currentCourses.toString());
        pastCoursesStr = removeBracketsArrStr(pastCourses.toString());
//        databaseReference.child("users").child(currentUsername).child("currentCourses").setValue(currentCoursesStr);
//        databaseReference.child("users").child(currentUsername).child("pastCourses").setValue(pastCoursesStr);
//        removeCurrentStudentFromCourse(course);
    }

    // Update courses upon course withdrawal
    private void withdrawCourse(String course) {
        currentCourses.remove(course); // List
        coursesWithdrawn.add(course);
        setCurrentCoursesViews();
        currentCoursesStr = removeBracketsArrStr(currentCourses.toString());
        // TODO: remove student from current student course list in DB
//        databaseReference.child("users").child(currentUsername).child("currentCourses").setValue(currentCoursesStr);
//        databaseReference.child("users").child(currentUsername).child("pastCourses").setValue(pastCoursesStr);
//        removeCurrentStudentFromCourse(course);
    }

    // Update courses upon course deletion
    private void deleteCourse(String course) {
        pastCourses.remove(course); // List
        coursesDeleted.add(course);
        setPastCoursesViews();
        pastCoursesStr = removeBracketsArrStr(pastCourses.toString());
        // TODO: remove student from mentor student course list in DB
//        databaseReference.child("users").child(currentUsername).child("currentCourses").setValue(currentCoursesStr);
//        databaseReference.child("users").child(currentUsername).child("pastCourses").setValue(pastCoursesStr);
//        removeCurrentStudentFromCourse(course);
    }

    private void completeCoursePassDB(String course) {
        databaseReference.child("users").child(currentUsername).child("currentCourses").setValue(currentCoursesStr);
        databaseReference.child("users").child(currentUsername).child("pastCourses").setValue(pastCoursesStr);
        removeCurrentStudentFromCourse(course);
    }


    // TODO: add check to determine if should call addMentorToCourse
    private void removeCurrentStudentFromCourse(final String course) {
        databaseReference.child("courses").child(course).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create Course object and update list of current students
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course dbCourse = snapshot.getValue(Course.class);
                    if (dbCourse != null) {
                        String currentStudents = dbCourse.getCurrentStudents();
                        if (currentStudents != null && !currentStudents.equals("")) {
                            List<String> currentStudentsList = dbCourse.convertStrToArray(currentStudents);
                            currentStudentsList.remove(currentUsername);
                            String modifiedCurrStudents = removeBracketsArrStr(currentStudentsList.toString());
                            databaseReference.child("courses").child(course).child("currentStudents").setValue(modifiedCurrStudents);
                        }
                    }
                }
                addMentorToCourse(course);
//                addUserToCourse2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addMentorToCourse(final String course) {
        databaseReference.child("courses").child(course).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create Course object and update list of current students
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course dbCourse = snapshot.getValue(Course.class);
                    if (dbCourse != null) {
                        String courseMentors = dbCourse.getMentors();
                        if (courseMentors == null || courseMentors.equals("")) {
                            databaseReference.child("courses").child(course).child("mentors").setValue(currentUsername);
                        } else {
                            databaseReference.child("courses").child(course).child("mentors").setValue(courseMentors + ", " + currentUsername);
                        }
                    }
                } else {
                    databaseReference.child("courses").child(course).child("mentors").setValue(currentUsername);
                }
//                addUserToCourse2();
//                setCurrentCoursesViews();
                setPastCoursesViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCurrentCoursesViews() {
        if (currentCourses.size() > 0) {
            currentCourse1View.setText(currentCourses.get(0));
            currentCourse1View.setVisibility(View.VISIBLE);
            withdrawButton1.setVisibility(View.VISIBLE);
            completeButton1.setVisibility(View.VISIBLE);
            if (currentCourses.size() > 1) {
                currentCourse2View.setText(currentCourses.get(1));
                currentCourse2View.setVisibility(View.VISIBLE);
                withdrawButton2.setVisibility(View.VISIBLE);
                completeButton2.setVisibility(View.VISIBLE);
            } else {
                currentCourse2View.setVisibility(View.GONE);
                withdrawButton2.setVisibility(View.GONE);
                completeButton2.setVisibility(View.GONE);
            }
        } else {
            currentCourse1View.setVisibility(View.GONE);
            withdrawButton1.setVisibility(View.GONE);
            completeButton1.setVisibility(View.GONE);

            currentCourse2View.setVisibility(View.GONE);
            withdrawButton2.setVisibility(View.GONE);
            completeButton2.setVisibility(View.GONE);
        }
    }


    private void setPastCoursesViews() {
        if (pastCourses.size() > 0) {
            pastCourse1View.setText(pastCourses.get(0));
            pastCourse1View.setVisibility(View.VISIBLE);
            checkBox1.setVisibility(View.VISIBLE);
            deleteButton1.setVisibility(View.VISIBLE);
            if (pastCourses.size() > 1) {
                pastCourse2View.setText(pastCourses.get(1));
                pastCourse2View.setVisibility(View.VISIBLE);
                checkBox2.setVisibility(View.VISIBLE);
                deleteButton2.setVisibility(View.VISIBLE);
                if (pastCourses.size() > 2) {
                    pastCourse3View.setText(pastCourses.get(2));
                    pastCourse3View.setVisibility(View.VISIBLE);
                    checkBox3.setVisibility(View.VISIBLE);
                    deleteButton3.setVisibility(View.VISIBLE);
                    if (pastCourses.size() > 3) {
                        pastCourse4View.setText(pastCourses.get(3));
                        pastCourse4View.setVisibility(View.VISIBLE);
                        checkBox4.setVisibility(View.VISIBLE);
                        deleteButton4.setVisibility(View.VISIBLE);
                    } else {
                        pastCourse4View.setVisibility(View.GONE);
                        checkBox4.setVisibility(View.GONE);
                        deleteButton4.setVisibility(View.GONE);
                    }
                } else {
                    pastCourse3View.setVisibility(View.GONE);
                    checkBox3.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);

                    pastCourse4View.setVisibility(View.GONE);
                    checkBox4.setVisibility(View.GONE);
                    deleteButton4.setVisibility(View.GONE);
                }
            } else {
                pastCourse2View.setVisibility(View.GONE);
                checkBox2.setVisibility(View.GONE);
                deleteButton2.setVisibility(View.GONE);

                pastCourse3View.setVisibility(View.GONE);
                checkBox3.setVisibility(View.GONE);
                deleteButton3.setVisibility(View.GONE);

                pastCourse4View.setVisibility(View.GONE);
                checkBox4.setVisibility(View.GONE);
                deleteButton4.setVisibility(View.GONE);
            }
        } else {
            pastCourse1View.setVisibility(View.GONE);
            checkBox1.setVisibility(View.GONE);
            deleteButton1.setVisibility(View.GONE);

            pastCourse2View.setVisibility(View.GONE);
            checkBox2.setVisibility(View.GONE);
            deleteButton2.setVisibility(View.GONE);

            pastCourse3View.setVisibility(View.GONE);
            checkBox3.setVisibility(View.GONE);
            deleteButton3.setVisibility(View.GONE);

            pastCourse4View.setVisibility(View.GONE);
            checkBox4.setVisibility(View.GONE);
            deleteButton4.setVisibility(View.GONE);
        }
    }

    // After running toString on an array, remove the brackets from the String
    private String removeBracketsArrStr(String arrayString) {
        String tempStr = arrayString.replaceAll("\\[", "");
        tempStr = tempStr.replaceAll("\\]", "");
        return tempStr;
    }
}