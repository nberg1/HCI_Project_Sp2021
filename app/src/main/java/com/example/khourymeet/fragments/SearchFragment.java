package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, NavigationFragment {

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

    private Map<String, String> studentNameList;

    private Button searchButton;
    private EditText typeSearch;
    private TextView studentName;
    private Button profileButton;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get views
        getViews();

        // Create empty map for student usernames : student names
        studentNameList = new HashMap<String, String>();

        // Add student names to list
        getListStudents();

        // TODO: add onclick for go to profile
        searchButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View complete) {
                        String nameSearched = typeSearch.getText().toString();
                        String usernameSearched = getKeyUsername(nameSearched);
                        // NOTE: this gets the first entry that matches the name and displays it
                        // For the next iteration of this project, add more views and change this
                        // to get a set of keys with the value
                        if (nameSearched != null) {
                            studentName.setText(studentNameList.get(usernameSearched));
                            studentName.setVisibility(View.VISIBLE);
                            profileButton.setVisibility(View.VISIBLE);
                        }

                    }
                }
        );


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getTitle() {
        return R.string.search_students;
    }

    private void getViews() {
        searchButton = getView().findViewById(R.id.search_button);
        typeSearch = getView().findViewById(R.id.searchView);
        studentName = getView().findViewById(R.id.search_friend_name);
        studentName.setVisibility(View.GONE);
        profileButton = getView().findViewById(R.id.friend_go_profile);
        profileButton.setVisibility(View.GONE);
    }

    private void getListStudents() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create User object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    User studentUser = childSnapshot.getValue(User.class);
                    if (!studentUser.getUserName().equals(currentUsername)) {
                        studentNameList.put(studentUser.getUserName(), studentUser.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("IN DB REFERENCE", "on cancelled");
            }
        });
    }

    // Citation: taken from https://www.baeldung.com/java-map-key-from-value
    public String getKeyUsername(String val) {
        String value = val.toUpperCase();
        for (Map.Entry<String, String> entry : studentNameList.entrySet()) {
            if (entry.getValue().toUpperCase().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}