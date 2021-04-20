
package com.example.khourymeet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.khourymeet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

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
    private RecyclerView rView;
    private ArrayList<MessageCard> messageList = new ArrayList<>();
    private MsgsRecyclerAdapter msgAdapter;
    private RecyclerView.LayoutManager layout;
    private FloatingActionButton addButton;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private Bundle savedInstanceState;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        rView = view.findViewById(R.id.recyclerview);
        rView.setHasFixedSize(true);
        msgAdapter = new MsgsRecyclerAdapter(messageList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                messageList.get(position).onItemClick(position);
            }
        };
        msgAdapter.setOnClickItemClickListener(itemClickListener);
        layout = new LinearLayoutManager(view.getContext());
        rView.setLayoutManager(layout);
        rView.setAdapter(msgAdapter);
        this.savedInstanceState = savedInstanceState;

        try {
            initialItemData(savedInstanceState);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initialItemData(Bundle savedInstanceState) throws MalformedURLException {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (messageList == null || messageList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                size = messageList.size();
                for (int i = 0; i < size; i++) {
                    Integer image = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    MessageCard sCard = new MessageCard("");
                    messageList.add(sCard);
                }
            }
        }
        // Load the initial cards
        else {
            MessageCard item1 = new MessageCard("Nicole");
            MessageCard item2 = new MessageCard("Alice");
            MessageCard item3 = new MessageCard("Brandon");
            MessageCard item4 = new MessageCard("Charles");
            messageList.add(item1);
            messageList.add(item2);
            messageList.add(item3);
            messageList.add(item4);
        }
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
        try {
            initialItemData(this.savedInstanceState);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getActivity().setTitle(getTitle());
    }
}