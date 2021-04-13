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

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, NavigationFragment {

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    private String currentUsername;
    private final String defaultString = "default";

    // Views for profile text
    private TextView nameView;
    private TextView usernameView;
    private TextView emailView;
    private TextView programView;
    private TextView currentCoursesView;
    private TextView pastCoursesView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Link user text info with a user object or a getting a snapshot of the user from db
    private Button editButton;
    
    // TODO: onClick of edit profile button directs to EditProfileFragment
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        createUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getTitle() {
        return R.string.my_profile;
    }

    private void setProfileText(User user) {
        // Get TextViews for profile texts
        nameView = getView().findViewById(R.id.name);
        usernameView = getView().findViewById(R.id.username);
        emailView = getView().findViewById(R.id.user_email);
        programView = getView().findViewById(R.id.user_program);
        currentCoursesView = getView().findViewById(R.id.user_currCourses);
        pastCoursesView = getView().findViewById(R.id.user_prevCourses);

        // Set texts for profile
        nameView.setText(user.getName());
        usernameView.setText(user.getUserName());
        emailView.setText(user.getEmail());
        programView.setText(createProgramString(user));
//        currentCoursesView.setText(createCoursesString(user, 1));
        currentCoursesView.setText(user.getCurrentCourseList());
        pastCoursesView.setText(createCoursesString(user, -1));

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
                    setProfileText(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Get string of courses
    // To get string of past courses, pass -1 for courseType
    // To get string of current courses, pass 1 for courseType
    private String createCoursesString(User user, int courseType) {
        List<String> courses;
        if (courseType == -1) {
            courses = user.getPastCourses();
        } else {
            courses = user.getCurrentCourses();
        }
        StringBuilder courseList;
        if (courses == null) {
            return "NONE";
        } else {
            courseList = new StringBuilder();
            int numCourses = courses.size();
            for (int i = 0; i < numCourses; i++) {
                courseList.append(courses.get(i));
                if (i != numCourses - 1) {
                    courseList.append(", ");
                }
            }
            return courseList.toString();
        }
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
}