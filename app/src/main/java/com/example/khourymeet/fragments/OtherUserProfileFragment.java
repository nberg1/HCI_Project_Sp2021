package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khourymeet.R;
import com.example.khourymeet.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherUserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherUserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    // Username of person searched for
    private String otherUsername;
    private final String defaultString = "default";

    // Views for profile text
    private TextView nameView;
    private TextView emailView;
    private TextView programView;
    private TextView currentCoursesView;
    private TextView pastCoursesView;

    public OtherUserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherUserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherUserProfileFragment newInstance(String param1, String param2) {
        OtherUserProfileFragment fragment = new OtherUserProfileFragment();
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
        otherUsername = sharedPreferences.getString(getString(R.string.other_username_preferences_key), defaultString);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        createUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_user_profile, container, false);
    }

    // Create User object from Database entry for the current username
    private void createUser() {
        databaseReference.child(getString(R.string.users_path,
                otherUsername)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create User object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User otherUser = snapshot.getValue(User.class);
                    setProfileText(otherUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Populate profile with data from database
    private void setProfileText(User user) {
        // Get TextViews for profile texts
        nameView = getView().findViewById(R.id.name);
        emailView = getView().findViewById(R.id.user_email);
        programView = getView().findViewById(R.id.user_program);
        currentCoursesView = getView().findViewById(R.id.user_currCourses);
        pastCoursesView = getView().findViewById(R.id.user_prevCourses);

        // Set texts for profile
        nameView.setText(user.getName());
        emailView.setText(user.getEmail());
        programView.setText(createProgramString(user));
        currentCoursesView.setText(getCoursesString(user, 1));
        pastCoursesView.setText(getCoursesString(user, -1));

    }


    // Create string for program with the form [Align/MSCS], [First Semester]
    private String createProgramString(User user) {
        StringBuilder programString = new StringBuilder();
        if (user.getAlign()) {
            programString.append("ALIGN, ");
        } else {
            programString.append("MSCS, ");
        }
        programString.append(user.getFirstSemester());
        return programString.toString();
    }

    // Get string of courses
    // To get string of past courses, pass -1 for courseType
    // To get string of current courses, pass 1 for courseType
    private String getCoursesString(User user, int courseType) {
        String courses;
        if (courseType == -1) {
            courses = user.getPastCourses();
        } else {
            courses = user.getCurrentCourses();
        }
        if (courses == null || courses.equals("")) {
            return "NONE";
        } else {
            return courses;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        createUser();
    }
}