package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khourymeet.NavigationFragment;
import com.example.khourymeet.R;
import com.example.khourymeet.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, NavigationFragment {

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    // TODO: use current username as key to pass academic info to db
    private String currentUsername;
    private final String defaultString = "default";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        createUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getTitle() {
        return R.string.home_page;
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
                    setCourseButtons(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Set course buttons for current courses
    private void setCourseButtons(User user) {
        // Get button views for course buttons
        Button course1Button = getView().findViewById(R.id.course1_button);
        Button course2Button = getView().findViewById(R.id.course2_button);
        List<String> courseList = user.convertStrToArray(user.getCurrentCourses());
        if (courseList == null || courseList.size() == 0) {
            TextView courseListTitle = getView().findViewById(R.id.course_title);
            courseListTitle.setText(getString(R.string.no_current_courses));
            course1Button.setVisibility(View.INVISIBLE);
            course2Button.setVisibility(View.INVISIBLE);
        } else {
            course1Button.setText(courseList.get(0));
            if (courseList.size() == 1) {
                course2Button.setVisibility(View.INVISIBLE);
            } else {
                course2Button.setText(courseList.get(1));
            }
        }
    }

}