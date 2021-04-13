package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.khourymeet.R;
import com.example.khourymeet.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

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

    // Create list of current courses
    private List<String> currentCourses;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);
        currentUsername = "marielleTest";

        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentCourses = new ArrayList<>();

        getTextViews();

        createUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


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
    }


    // Create User object from Database entry for the current username
    private void createUser() {
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
        currentCourses = user.convertStrToArray(user.getCurrentCourses());

        // Populate current course views
        setCurrentCoursesViews();

//        programView.setText(createProgramString(user));
//        currentCoursesView.setText(createCoursesString(user, 1));
//        pastCoursesView.setText(createCoursesString(user, -1));

    }

    // Get string of courses
    // To get string of past courses, pass -1 for courseType
    // To get string of current courses, pass 1 for courseType
//    private List<String> createCoursesList(User user, int courseType) {
//        List<String> courses;
//        if (courseType == -1) {
//            courses = user.getPastCourses();
//        } else {
//            courses = user.getCurrentCourses();
//        }
//        return courses;
//    }

    private void setCurrentCoursesViews() {
        if (currentCourses.size() < 1) {
            currentCourse1View.setText("NONE");
            currentCourse2View.setText("NONE");
        } else if (currentCourses.size() == 1) {
            currentCourse1View.setText(currentCourses.get(0));
            currentCourse2View.setText("NONE");
        } else {
            currentCourse1View.setText(currentCourses.get(0));
            currentCourse2View.setText(currentCourses.get(1));
        }
    }
}