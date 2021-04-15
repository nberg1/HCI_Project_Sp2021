package com.example.khourymeet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khourymeet.R;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursePageFragment extends Fragment implements  View.OnClickListener, NavigationFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rView;
    private ArrayList<StudentCard> studentList = new ArrayList<>();
    private StudentsRecyclerAdapter studentAdapter;
    private GridLayoutManager layout;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    public CoursePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursePageFragment newInstance(String param1, String param2) {
        CoursePageFragment fragment = new CoursePageFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_page, container, false);

        rView = view.findViewById(R.id.studentrecyclerview);
        rView.setHasFixedSize(true);
        studentAdapter = new StudentsRecyclerAdapter(studentList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                studentList.get(position).onItemClick(position);
            }
        };
        studentAdapter.setOnClickItemClickListener(itemClickListener);
        layout = new GridLayoutManager(view.getContext(), 2);
        rView.setLayoutManager(layout);
        rView.setAdapter(studentAdapter);

        try {
            initialItemData(savedInstanceState);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initialItemData(Bundle savedInstanceState) throws MalformedURLException {
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (studentList == null || studentList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                size = studentList.size();
                for (int i = 0; i < size; i++) {
                    Integer image = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    StudentCard sCard = new StudentCard("");
                    studentList.add(sCard);
                }
            }
        }
        // Load the initial cards
        else {
            StudentCard item1 = new StudentCard("Nicole");
            StudentCard item2 = new StudentCard("Alice");
            StudentCard item3 = new StudentCard("Brandon");
            StudentCard item4 = new StudentCard("Charles");
            studentList.add(item1);
            studentList.add(item2);
            studentList.add(item3);
            studentList.add(item4);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getTitle() {
        return R.string.current_courses;
    }
}