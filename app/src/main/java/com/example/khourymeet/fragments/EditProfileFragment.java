package com.example.khourymeet.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khourymeet.AcademicInfoActivity;
import com.example.khourymeet.Course;
import com.example.khourymeet.MainActivity;
import com.example.khourymeet.R;
import com.example.khourymeet.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    private String currentUsername;
    private final String defaultString = "default";

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

    // EditTextViews for changeable user information
    private EditText editName;
    private EditText editPassword;

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

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentCourses = new ArrayList<>();
        pastCourses = new ArrayList<>();
        coursesAdded = new ArrayList<>();
        coursesCompleted = new ArrayList<>();
        coursesWithdrawn= new ArrayList<>();
        coursesDeleted = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getButtonViews();

        getTextViews();

        createUserSetText();

//         Complete Course1
        completeButton1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse1 = getView().findViewById(R.id.user_currCourse1);
                        String course = currCourse1.getText().toString();
                        markCourseCompleted(course);
                    }
                }
        );
        // Complete Course2
        completeButton2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse2 = getView().findViewById(R.id.user_currCourse2);
                        String course = currCourse2.getText().toString();
                        markCourseCompleted(course);
                    }
                }
        );
        // Withdraw Course1
        withdrawButton1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse1 = getView().findViewById(R.id.user_currCourse1);
                        String course = currCourse1.getText().toString();
                        markCourseWithdrawn(course);
                    }
                }
        );
        // Withdraw Course2
        withdrawButton2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView currCourse2 = getView().findViewById(R.id.user_currCourse2);
                        String course = currCourse2.getText().toString();
                        markCourseWithdrawn(course);
                    }
                }
        );
        // Delete Past Course1
        deleteButton1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = getView().findViewById(R.id.user_prevCourse1);
                        String course = pastCourse.getText().toString();
                        markCourseDeleted(course);
                    }
                }
        );
        // Delete Past Course2
        deleteButton2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = getView().findViewById(R.id.user_prevCourse2);
                        String course = pastCourse.getText().toString();
                        markCourseDeleted(course);
                    }
                }
        );
        // Delete Past Course3
        deleteButton3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = getView().findViewById(R.id.user_prevCourse3);
                        String course = pastCourse.getText().toString();
                        markCourseDeleted(course);
                    }
                }
        );
        // Delete Past Course4
        deleteButton4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        TextView pastCourse = getView().findViewById(R.id.user_prevCourse4);
                        String course = pastCourse.getText().toString();
                        markCourseDeleted(course);
                    }
                }
        );

        // Mark course invisible
        checkBox1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        markCourseInvisible();
                    }
                }
        );
        // Mark course invisible
        checkBox2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        markCourseInvisible();
                    }
                }
        );
        // Mark course invisible
        checkBox3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        markCourseInvisible();
                    }
                }
        );
        // Mark course invisible
        checkBox4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        markCourseInvisible();
                    }
                }
        );

        // Add course
        addCourseButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        if (currentCourses.size() >= 2) {
                            maxCoursesListed();
                        } else {
                            // TODO: dialog box to add course
                            addCoursesDialog();
                        }
                    }
                }
        );


        // Save
        saveButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        // TODO: confirmation dialog
                        saveStudentInfoToDb();
                        updateCourseLists();
                        Log.w("currentCourses", currentCoursesStr);
                        Log.w("pastCourses", pastCoursesStr);
                    }
                }
        );

    }

    // Get all Button views
    private void getButtonViews() {
        addCourseButton = getView().findViewById(R.id.button4);
        saveButton = getView().findViewById(R.id.save_button);

        withdrawButton1 = getView().findViewById(R.id.withdraw_btn1);
        withdrawButton2 = getView().findViewById(R.id.withdraw_btn2);

        completeButton1 = getView().findViewById(R.id.complete_btn1);
        completeButton2 = getView().findViewById(R.id.complete_btn2);

        deleteButton1 = getView().findViewById(R.id.delete_btn1);
        deleteButton2 = getView().findViewById(R.id.delete_btn2);
        deleteButton3 = getView().findViewById(R.id.delete_btn3);
        deleteButton4 = getView().findViewById(R.id.delete_btn4);

        checkBox1 = getView().findViewById(R.id.checkBox);
        checkBox2 = getView().findViewById(R.id.checkBox2);
        checkBox3 = getView().findViewById(R.id.checkBox3);
        checkBox4 = getView().findViewById(R.id.checkBox4);
    }

    // Get all Text and EditText views
    private void getTextViews() {
        // Get EditText views for profile texts
        editName = getView().findViewById(R.id.name);
        editPassword = getView().findViewById(R.id.password);

        // Get TextViews for profile texts
        usernameView = getView().findViewById(R.id.username);
        emailView = getView().findViewById(R.id.user_email);

        // Get TextViews for current courses
        currentCourse1View = getView().findViewById(R.id.user_currCourse1);
        currentCourse2View = getView().findViewById(R.id.user_currCourse2);

        // Get TextViews for past courses
        pastCourse1View = getView().findViewById(R.id.user_prevCourse1);
        pastCourse2View = getView().findViewById(R.id.user_prevCourse2);
        pastCourse3View = getView().findViewById(R.id.user_prevCourse3);
        pastCourse4View = getView().findViewById(R.id.user_prevCourse4);
    }

    // Create User object from Database entry for the current username and set the edit profile text
    private void createUserSetText() {
        Log.w("IN SET TEXT", currentUsername);
        databaseReference.child(getString(R.string.users_path,
                currentUsername)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create User object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("IN DB REFERENCE", "on data change");
                if (snapshot.exists()) {
                    User currentUser = snapshot.getValue(User.class);
                    if (currentUser != null) {
                        setEditProfileText(currentUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("IN DB REFERENCE", "on cancelled");
            }
        });
    }

    // Use the user object from the database to populate info in profile
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

    // Add course
    private void addCourse(String course) {
        currentCourses.add(course);
        setCurrentCoursesViews();
        currentCoursesStr = removeBracketsArrStr(currentCourses.toString());
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
    }

    // Update courses upon course withdrawal
    private void withdrawCourse(String course) {
        currentCourses.remove(course); // List
        coursesWithdrawn.add(course);
        setCurrentCoursesViews();
        currentCoursesStr = removeBracketsArrStr(currentCourses.toString());
    }

    // Update courses upon course deletion
    private void deleteCourse(String course) {
        pastCourses.remove(course); // List
        coursesDeleted.add(course);
        setPastCoursesViews();
        pastCoursesStr = removeBracketsArrStr(pastCourses.toString());
    }

    // Update student info in database
    private void saveStudentInfoToDb() {
        databaseReference.child("users").child(currentUsername).child("name").setValue(editName.getText().toString());
        databaseReference.child("users").child(currentUsername).child("password").setValue(editPassword.getText().toString());
        databaseReference.child("users").child(currentUsername).child("currentCourses").setValue(currentCoursesStr);
        databaseReference.child("users").child(currentUsername).child("pastCourses").setValue(pastCoursesStr);
    }

    // Update list of current students/mentors in database
    private void updateCourseLists() {
        databaseReference.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to go through courses
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course dbCourse = childSnapshot.getValue(Course.class);
                    String dbCourseName = dbCourse.getCourseName();
                    // TODO: Null checks ?
                    List<String> currStudentList = dbCourse.convertStrToArray(dbCourse.getCurrentStudents());
                    if (currStudentList == null) {
                        currStudentList = new ArrayList<>();
                    }
                    List<String> currMentorList = dbCourse.convertStrToArray(dbCourse.getMentors());
                    if (currMentorList == null) {
                        currMentorList = new ArrayList<>();
                    }
                    // If course is in list of completed courses, update list of mentors and current students
                    if (coursesCompleted.contains(dbCourseName)) {
                        // Remove student from list of current students
                        currStudentList.remove(currentUsername);
                        // Add student to list of mentors
                        currMentorList.add(currentUsername);
                    }
                    // If course is in list of completed courses, remove student from list of mentors
                    if (coursesDeleted.contains(dbCourseName)) {
                        currMentorList.remove(currentUsername);
                    }
                    // If course is in list of withdrawn courses, remove student from list of current students
                    if (coursesWithdrawn.contains(dbCourseName)) {
                        currStudentList.remove(currentUsername);
                    }
                    // If course is in list of courses added, add student to list of current courses
                    if (coursesAdded.contains(dbCourseName)) {
                        currStudentList.add(currentUsername);
                    }
                    String newCurrStudentStr = removeBracketsArrStr(currStudentList.toString());
                    String newMentorStr = removeBracketsArrStr(currMentorList.toString());
                    databaseReference.child("courses").child(dbCourseName).child("currentStudents").setValue(newCurrStudentStr);
                    databaseReference.child("courses").child(dbCourseName).child("mentors").setValue(newMentorStr);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Set up dialog box for "mark as complete"
    private void markCourseCompleted(final String course) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.mark_complete_warning, course));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.mark_complete_yes, course), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeCourse(course);
            }
        });
        builder.setNegativeButton(getString(R.string.mark_complete_no, course), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.RED);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
    }

    // Set up dialog box for "withdraw"
    private void markCourseWithdrawn(final String course) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.withdraw_warning, course, course));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.withdraw_yes, course), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                withdrawCourse(course);
            }
        });
        builder.setNegativeButton(getString(R.string.withdraw_no, course), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.RED);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
    }

    // Set up dialog box for "delete"
    private void markCourseDeleted(final String course) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.delete_warning, course));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.delete_yes, course), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCourse(course);
            }
        });
        builder.setNegativeButton(getString(R.string.delete_no, course), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.RED);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
    }

    // Set up dialog box for "invisible"
    // When functionality is implemented, method signature will be "private void markCourseInvisible(String course)"
    private void markCourseInvisible() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.invisible_explanation));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.invisible_understand), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When functionality is implemented -- remove student from list of mentors, remove course from list of publicly viewable course
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
    }

    // Set up dialog box to display if you already have two courses
    private void maxCoursesListed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.too_many_courses));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.go_back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.RED);
    }

    // Note: this uses the TutorialsPoint example for creating a single-choice dialog
    private void addCoursesDialog() {
        final String[] items = {"CS5001", "CS5003", "CS5004", "CS5006"};
        int checkedItem = 0;
        final String[] courseChosen = {"CS5001"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.add_course));
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentCourses.contains(items[which])) {
                    courseChosen[0] = items[which];
                    Toast.makeText(getContext(), getString(R.string.already_taking, items[which]), Toast.LENGTH_LONG).show();
                } else if (pastCourses.contains(items[which])){
                    courseChosen[0] = items[which];
                    Toast.makeText(getContext(), getString(R.string.took_before, items[which]), Toast.LENGTH_LONG).show();
                } else {
                    courseChosen[0] = items[which];
                }
            }
        });
        builder.setPositiveButton(getString(R.string.add_course_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentCourses.contains(courseChosen[0])) {
                    Toast.makeText(getContext(), getString(R.string.not_added_current, courseChosen[0]), Toast.LENGTH_LONG).show();
                } else if (pastCourses.contains(courseChosen[0])) {
                    Toast.makeText(getContext(), getString(R.string.not_added_past, courseChosen[0]), Toast.LENGTH_LONG).show();
                } else {
                    addCourse(courseChosen[0]);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.add_course_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.RED);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
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