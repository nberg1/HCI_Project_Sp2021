package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link NewMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewMessageFragment extends Fragment {

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
    private List<String> studentNameIntermediate;
//    private String[] studentNameArr;

//    private Button addStudentButton;
    private AutoCompleteTextView typeSearch;
    private EditText typeMessage;
    private Button sendMessage;
    int messageNum;
    private String usernameSearched;
    // Views for messages
    CardView cardViewMessage1;
    TextView message1;
    CardView cardViewMessage2;
    TextView message2;
    CardView cardViewMessage3;
    TextView message3;

    public NewMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewMessageFragment newInstance(String param1, String param2) {
        NewMessageFragment fragment = new NewMessageFragment();
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

        messageNum = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_message2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get views
        getViews();

        // Create empty map for student usernames : student names
        studentNameList = new HashMap<String, String>();
        // Create empty list for student names
        studentNameIntermediate = new ArrayList<>();

        // Add student names to list and map
        getListStudents();

//        // Create array from list
//        studentNameArr = studentNameIntermediate.toArray(studentNameArr);
//
//        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, studentNameArr);
//        typeSearch.setAdapter(adapter);
//        typeSearch.setThreshold(1);

//        addStudentButton.setOnClickListener(
//                new Button.OnClickListener() {
//                    public void onClick(View complete) {
//                        String nameSearched = typeSearch.getText().toString();
//                        usernameSearched = getKeyUsername(nameSearched);
//                        // NOTE: this gets the first entry that matches the name and displays it
//                        // For the next iteration of this project, add more views and change this
//                        // to get a set of keys with the value
//                        if (usernameSearched != null) {
//                            // Referenced Android documentation to write username locally
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString(getString(R.string.other_username_preferences_key), usernameSearched);
//                            editor.apply();
//                        }
//                    }
//                }
//        );

        sendMessage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View complete) {
                String nameSearched = typeSearch.getText().toString();
                usernameSearched = getKeyUsername(nameSearched);
                if (messageNum == 0) {
                    addFriend();
                    setMessage1();
                } else if (messageNum == 1) {
                    setMessage2();
                }
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

    private void getViews() {
//        addStudentButton = getView().findViewById(R.id.add_button);
        typeSearch = getView().findViewById(R.id.autocompleteMessageView);
        typeMessage = getView().findViewById(R.id.write_message);
        sendMessage = getView().findViewById(R.id.send_button);
        cardViewMessage1 = getView().findViewById(R.id.message_card1);
        message1 = getView().findViewById(R.id.out_text1);
        cardViewMessage2 = getView().findViewById(R.id.message_card2);
        message2 = getView().findViewById(R.id.in_text);
        cardViewMessage3 = getView().findViewById(R.id.message_card3);
        message3 = getView().findViewById(R.id.out_text2);
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
                        studentNameIntermediate.add(studentUser.getName());
                    }
                }
                // Create array from list
                String[] studentNameArr = new String[studentNameIntermediate.size()];
                studentNameArr = studentNameIntermediate.toArray(studentNameArr);

                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, studentNameArr);
                typeSearch.setAdapter(adapter);
                typeSearch.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Add other user to list of friends
    private void addFriend() {
        if (usernameSearched != null) {
            databaseReference.child(getString(R.string.users_path,
                    currentUsername)).addListenerForSingleValueEvent(new ValueEventListener() {
                // Use snapshot to create User object
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User currentUser = snapshot.getValue(User.class);
                        if (currentUser != null) {
                            String currFriendsStr = currentUser.getFriends();
                            if (currFriendsStr != null && !currFriendsStr.equals("")) {
                                List<String> currFriendsList = currentUser.convertStrToArray(currFriendsStr);
                                if (!currFriendsList.contains(usernameSearched)) {
                                    currFriendsList.add(usernameSearched);
                                    String newFriendsStr = currentUser.removeBracketsArrStr(currFriendsList.toString());
                                    databaseReference.child("users").child(currentUsername).child("friends").setValue(newFriendsStr);
                                }
                            } else {
                                databaseReference.child("users").child(currentUsername).child("friends").setValue(usernameSearched);
                            }
                        }
                    }
                    messageNum += 1;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast toast = Toast.makeText(getContext(), "Please select a user from the suggestions in the search.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    public void setMessage1() {
        String message = typeMessage.getText().toString();
        message1.setText(message);
        cardViewMessage1.setVisibility(View.VISIBLE);
        typeMessage.getText().clear();
    }

    public void setMessage2() {
        String message = typeMessage.getText().toString();
        message3.setText(message);
        cardViewMessage3.setVisibility(View.VISIBLE);
        typeMessage.getText().clear();
    }
}