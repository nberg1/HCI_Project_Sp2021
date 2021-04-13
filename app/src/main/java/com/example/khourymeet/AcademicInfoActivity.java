package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcademicInfoActivity extends AppCompatActivity implements MultiSelectSpinner.OnMultipleItemsSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String currentUsername;
    private final String defaultString = "default";
    private RadioGroup radioGroup;
    private RadioButton alignRadioButton;
    private RadioButton mscsRadioButton;
    private Spinner spinner;
    private String firstSemester;
    private MultiSelectSpinner multiSelectSpinner;
    private String course1;
    private String course2;
    private String courses;

    private static final String TAG = "weird";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_info);

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        radioGroup = (RadioGroup) findViewById(R.id.group);
        alignRadioButton = (RadioButton) findViewById(R.id.align);
        mscsRadioButton = (RadioButton) findViewById(R.id.mscs);

        addOnSpinnerSemester();
        addOnSpinnerCourses();
    }

    public void addOnSpinnerSemester() {
        spinner = (Spinner) findViewById(R.id.semesters);
        ArrayList<String> list = new ArrayList<>();

        list.add("Choose Semester");
        list.add("Spring 2021");
        list.add("Fall 2020");
        list.add("Spring 2020");
        list.add("Fall 2019");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }

    public void addOnSpinnerCourses() {
        String[] array = {"5001", "5002", "5004", "5006"};
        multiSelectSpinner = (MultiSelectSpinner) findViewById(R.id.courses);
        multiSelectSpinner.setItems(array);
        multiSelectSpinner.setSelection(new int[]{0});
        multiSelectSpinner.setListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AcademicInfoActivity.this);
        builder.setMessage("Are you sure you want to exit the registration process? All data will be deleted");
        builder.setCancelable(true);
        builder.setPositiveButton("Continue registration", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Exit registration", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Deletes user entry
                databaseReference.child("users").child(currentUsername).removeValue();

                Intent intent = new Intent(AcademicInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.RED);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
    }

    // After inputting legal academic information, Save Profile redirects users to the Home page
    public void onClickHome(View view) {
        Boolean academicProgram = academicProgramCheck();
        if (academicProgram) {
            if (alignRadioButton.isChecked()) {
                databaseReference.child("users").child(currentUsername).child("align").setValue(true);
            } else {
                databaseReference.child("users").child(currentUsername).child("align").setValue(false);
            }

            Boolean firstSemesterInput = firstSemesterCheck();
            if (firstSemesterInput) {
                databaseReference.child("users").child(currentUsername).child("firstSemester").setValue(firstSemester);


                Boolean coursesInput = coursesCheck();
                if (coursesInput) {
                    databaseReference.child("users").child(currentUsername).child("currentCourses").setValue(courses);
                    addUserToCourse1();

//                    Intent intent = new Intent(AcademicInfoActivity.this, HomeActivity.class);
//                    startActivity(intent);
                }
            }
        }
    }

    private void addUserToCourse1() {
        databaseReference.child("courses").child(course1).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create Course object and update list of current students
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course dbCourse1 = snapshot.getValue(Course.class);
                    String courseStudents = dbCourse1.getCurrentStudents();
                    if (courseStudents == null || courseStudents.equals("")) {
                        databaseReference.child("courses").child(course1).child("currentStudents").setValue(currentUsername);
                    } else {
                        databaseReference.child("courses").child(course1).child("currentStudents").setValue(courseStudents + ", " + currentUsername);
                    }
                } else {
                    databaseReference.child("courses").child(course1).child("currentStudents").setValue(currentUsername);
                }
                addUserToCourse2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addUserToCourse2() {
        databaseReference.child("courses").child(course2).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create Course object and update list of current students
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course dbCourse2 = snapshot.getValue(Course.class);
                    String courseStudents = dbCourse2.getCurrentStudents();
                    if (courseStudents == null || courseStudents == "") {
                        databaseReference.child("courses").child(course2).child("currentStudents").setValue(currentUsername);
                    } else {
                        databaseReference.child("courses").child(course2).child("currentStudents").setValue(courseStudents + currentUsername);
                    }
                } else {
                    databaseReference.child("courses").child(course2).child("currentStudents").setValue(currentUsername);
                }
                Intent intent = new Intent(AcademicInfoActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Checks that a user has clicked on one of the radio buttons to select an academic program
    public boolean academicProgramCheck() {
        if (alignRadioButton.isChecked() || mscsRadioButton.isChecked()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Please select an academic program", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // Checks that a user has selected one of the semesters in the spinner list
    public boolean firstSemesterCheck() {
        firstSemester = spinner.getSelectedItem().toString();

        if (firstSemester == "Choose Semester") {
            Toast.makeText(getApplicationContext(), "Please select your first semester", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    // Checks that a user has selected two courses
    public boolean coursesCheck() {
        String[] items = multiSelectSpinner.getSelectedStrings().toArray(new String[0]);

        if (items.length == 2) {
            course1 = "CS" + items[0];
            course2 = "CS" + items[1];
            courses = course1 + ", " + course2;
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Please select two courses", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}