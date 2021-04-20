
package com.example.khourymeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khourymeet.Course;
import com.example.khourymeet.R;
import com.example.khourymeet.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements View.OnClickListener, NavigationFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // RecyclerView
//    private RecyclerView rView;
//    private ArrayList<MessageCard> messageList = new ArrayList<>();
//    private MsgsRecyclerAdapter msgAdapter;
//    private RecyclerView.LayoutManager layout;
//    private FloatingActionButton addButton;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private Bundle savedInstanceState;

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String currentUsername;
    private final String defaultString = "default";

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

//        rView = view.findViewById(R.id.recyclerview);
//        rView.setHasFixedSize(true);
//        msgAdapter = new MsgsRecyclerAdapter(messageList);
//        ItemClickListener itemClickListener = new ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                messageList.get(position).onItemClick(position);
//            }
//        };
//        msgAdapter.setOnClickItemClickListener(itemClickListener);
//        layout = new LinearLayoutManager(view.getContext());
//        rView.setLayoutManager(layout);
//        rView.setAdapter(msgAdapter);
//        this.savedInstanceState = savedInstanceState;
//
//        try {
//            initialItemData(savedInstanceState);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFriends();
        getActivity().setTitle(getTitle());
    }

    private void getFriends() {
        databaseReference.child("users").child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create User object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User currentUser = snapshot.getValue(User.class);
                    // gets you a list of friends
                    String friends = currentUser.getFriends();
                    // Note: will only display max of 4 conversations
                    final ArrayList friendCountList = new ArrayList();
                    if (friends != null && !friends.equals("")) {
                        final List<String> currFriendsList = currentUser.convertStrToArray(friends);
                        // get the names
                        for (String friend : currFriendsList) {
                            databaseReference.child(getString(R.string.users_path, friend)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User newFriend = dataSnapshot.getValue(User.class);
                                    String friendUsername = newFriend.getUserName();
                                    currFriendsList.add(friendUsername);
                                    // set the text view for each student conversation
                                    displayFriend(getView(), newFriend, currFriendsList.indexOf(friendUsername));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });
                        }
                    }
                    else {
                        noFriends(getView());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void initialItemData(Bundle savedInstanceState) throws MalformedURLException {
//        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
//            if (messageList == null || messageList.size() == 0) {
//
//                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
//                size = messageList.size();
//                for (int i = 0; i < size; i++) {
//                    Integer image = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
//                    MessageCard sCard = new MessageCard("");
//                    messageList.add(sCard);
//                }
//            }
//        }
//        // Load the initial cards
//        else {
//            MessageCard item1 = new MessageCard("Nicole");
//            MessageCard item2 = new MessageCard("Alice");
//            MessageCard item3 = new MessageCard("Brandon");
//            MessageCard item4 = new MessageCard("Charles");
//            messageList.add(item1);
//            messageList.add(item2);
//            messageList.add(item3);
//            messageList.add(item4);
//        }
//    }

    private void displayFriend(View view, User friend, int friendNum) {
        switch (friendNum) {
            case 0:
                view.findViewById(R.id.conversation1).setVisibility(View.VISIBLE);
                TextView nameText1 = view.findViewById(R.id.friend_name_convo1);
                nameText1.setText(friend.getName());
                TextView usernameText1 = view.findViewById(R.id.dm_username_hidden1);
                usernameText1.setText(friend.getUserName());
                break;
            case 1:
                view.findViewById(R.id.conversation2).setVisibility(View.VISIBLE);
                TextView nameText2 = view.findViewById(R.id.friend_name_convo2);
                nameText2.setText(friend.getName());
                TextView usernameText2 = view.findViewById(R.id.dm_username_hidden2);
                usernameText2.setText(friend.getUserName());
                break;
            case 2:
                view.findViewById(R.id.conversation3).setVisibility(View.VISIBLE);
                TextView nameText3 = view.findViewById(R.id.friend_name_convo3);
                nameText3.setText(friend.getName());
                TextView usernameText3 = view.findViewById(R.id.dm_username_hidden3);
                usernameText3.setText(friend.getUserName());
                break;
            case 3:
                view.findViewById(R.id.conversation4).setVisibility(View.VISIBLE);
                TextView nameText4 = view.findViewById(R.id.friend_name_convo4);
                nameText4.setText(friend.getName());
                TextView usernameText4 = view.findViewById(R.id.dm_username_hidden4);
                usernameText4.setText(friend.getUserName());
                break;
        }
    }

    private void noFriends(View view) {
        view.findViewById(R.id.conversation1).setVisibility(View.VISIBLE);
        TextView nameText1 = view.findViewById(R.id.friend_name_convo1);
        nameText1.setText("You do not have any direct messages.");
        view.findViewById(R.id.open_convo1).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getTitle() {
        return R.string.direct_messages;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriends();
//        try {
//            initialItemData(this.savedInstanceState);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        getActivity().setTitle(getTitle());
    }
}