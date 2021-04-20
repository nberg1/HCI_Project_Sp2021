package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Use the {@link DirectMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectMessageFragment extends Fragment {

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
    // Name of person searched for
    private String name;

    // Name of current user
    private String currentUsername;

    int messageNum;

    // Views for messages
    CardView cardViewMessage1;
    TextView message1;
    CardView cardViewMessage2;
    TextView message2;
    CardView cardViewMessage3;
    TextView message3;
    EditText writeMessage;
    Button sendButton;

    public DirectMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectMessageFragment newInstance(String param1, String param2) {
        DirectMessageFragment fragment = new DirectMessageFragment();
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
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        messageNum = 0;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_direct_message, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMessageViews();
        createUser();

        sendButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View complete) {
                if (messageNum == 0) {
                    addFriend();
                    setMessage1();
                } else if (messageNum == 1) {
                    setMessage2();
                }
            }
        });
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
                    setMessageHeaderText(otherUser);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // TODO: error saying "null object reference"
    // Get views for messages
    private void getMessageViews() {
        cardViewMessage1 = getView().findViewById(R.id.dm_message1);
        cardViewMessage1.setVisibility(View.GONE);
        message1 = getView().findViewById(R.id.out_text1);

        cardViewMessage2 = getView().findViewById(R.id.dm_message2);
        cardViewMessage2.setVisibility(View.GONE);
        message2 = getView().findViewById(R.id.in_text);

        cardViewMessage3 = getView().findViewById(R.id.dm_message3);
        cardViewMessage3.setVisibility(View.GONE);
        message3 = getView().findViewById(R.id.out_text2);

        writeMessage = getView().findViewById(R.id.write_message);
        sendButton = getView().findViewById(R.id.send_button);
    }

    // Populate message screen with data from database
    private void setMessageHeaderText(User user) {
        // Get TextViews for name
        TextView nameView = getView().findViewById(R.id.friend_name);
        // Set TextView for name
        name = user.getName();
        nameView.setText(name);
    }

    // Add other user to list of friends
    private void addFriend() {
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
                            if (!currFriendsList.contains(otherUsername)) {
                                currFriendsList.add(otherUsername);
                                String newFriendsStr = currentUser.removeBracketsArrStr(currFriendsList.toString());
                                databaseReference.child("users").child(currentUsername).child("friends").setValue(newFriendsStr);
                            }
                        } else {
                            databaseReference.child("users").child(currentUsername).child("friends").setValue(otherUsername);
                        }
                    }
                }
                messageNum += 1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setMessage1() {
        String message = writeMessage.getText().toString();
        message1.setText(message);
        cardViewMessage1.setVisibility(View.VISIBLE);
        writeMessage.getText().clear();
    }

    public void setMessage2() {
        String message = writeMessage.getText().toString();
        message3.setText(message);
        cardViewMessage3.setVisibility(View.VISIBLE);
        writeMessage.getText().clear();
    }

}