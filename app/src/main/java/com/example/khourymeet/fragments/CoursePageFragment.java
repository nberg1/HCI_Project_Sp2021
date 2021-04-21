package com.example.khourymeet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khourymeet.Course;
import com.example.khourymeet.R;
import com.example.khourymeet.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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
    private String courseName;
    private String mParam2;

    private RecyclerView rView;
    private ArrayList<StudentCard> studentList = new ArrayList<>();
    private StudentsRecyclerAdapter studentAdapter;
    private GridLayoutManager layout;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    private DatabaseReference databaseReference;

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
        this.courseName = mParam1;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_page, container, false);
        return view;
    }

//    public void createRecyclerView(View view, User student, boolean mentor) {
//        rView = view.findViewById(R.id.studentrecyclerview);
//        rView.setHasFixedSize(true);
//        studentAdapter = new StudentsRecyclerAdapter(studentList);
//        ItemClickListener itemClickListener = new ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                studentList.get(position).onItemClick(position);
//
//            }
//        };
//        studentAdapter.setOnClickItemClickListener(itemClickListener);
//        layout = new GridLayoutManager(view.getContext(), 2);
//        rView.setLayoutManager(layout);
//        rView.setAdapter(studentAdapter);
//        StudentCard card = new StudentCard(student.getName(), student.getUserName(), mentor);
//        // if a student is a mentor, setText on student card to show mentor text as visible
//        studentList.add(card);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCourse();
        getActivity().setTitle(getTitle());
    }


    private void createCourse() {
        Log.w("Entering createCourse: ", courseName);
        databaseReference.child("courses").child(courseName).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create User object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Course course = snapshot.getValue(Course.class);
                    // gets you a list of student usernames
                    String currentStudents = course.getCurrentStudents();
                    String mentorStudents = course.getMentors();
                    // Note: will only display max of 6 students
                    final ArrayList studentCountList = new ArrayList();
                    if (currentStudents != null && !currentStudents.equals("")) {
                        List<String> currStudentList = course.convertStrToArray(currentStudents);
                        // get the names
                        for (String user : currStudentList) {
                            databaseReference.child(getString(R.string.users_path, user)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User student = dataSnapshot.getValue(User.class);
                                    String studentUsername = student.getUserName();
                                    studentCountList.add(studentUsername);
                                    // set the text view/ create each individual student card
                                    displayStudent(getView(), student, studentCountList.indexOf(studentUsername), false);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });
                        }

                    }
                    if (mentorStudents != null && !mentorStudents.equals("")) {
                        List<String> mentorStudentList = course.convertStrToArray(mentorStudents);
                        // get the names
                        for (String user : mentorStudentList) {
                            databaseReference.child(getString(R.string.users_path, user)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User student = dataSnapshot.getValue(User.class);
                                    String studentUsername = student.getUserName();
                                    studentCountList.add(studentUsername);
                                    // set the text view/ create each individual student card
                                    displayStudent(getView(), student, studentCountList.indexOf(studentUsername), true);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayStudent(View view, User student, int studentNum, boolean mentor) {
        switch (studentNum) {
            case 0:
                getView().findViewById(R.id.student_card1).setVisibility(View.VISIBLE);
                TextView nameText1 = getView().findViewById(R.id.name1);
                nameText1.setText(student.getName());
                TextView usernameText1 = getView().findViewById(R.id.username_hidden1);
                usernameText1.setText(student.getUserName());
                if (mentor) {
                    getView().findViewById(R.id.mentor1).setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                getView().findViewById(R.id.student_card2).setVisibility(View.VISIBLE);
                TextView nameText2 = getView().findViewById(R.id.name2);
                nameText2.setText(student.getName());
                TextView usernameText2 = getView().findViewById(R.id.username_hidden2);
                usernameText2.setText(student.getUserName());
                if (mentor) {
                    getView().findViewById(R.id.mentor2).setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                getView().findViewById(R.id.student_card3).setVisibility(View.VISIBLE);
                TextView nameText3 = getView().findViewById(R.id.name3);
                nameText3.setText(student.getName());
                TextView usernameText3 = getView().findViewById(R.id.username_hidden3);
                usernameText3.setText(student.getUserName());
                if (mentor) {
                    getView().findViewById(R.id.mentor3).setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                getView().findViewById(R.id.student_card4).setVisibility(View.VISIBLE);
                TextView nameText4 = getView().findViewById(R.id.name4);
                nameText4.setText(student.getName());
                TextView usernameText4 = getView().findViewById(R.id.username_hidden4);
                usernameText4.setText(student.getUserName());
                if (mentor) {
                    getView().findViewById(R.id.mentor4).setVisibility(View.VISIBLE);
                }
                break;
            case 4:
                getView().findViewById(R.id.student_card5).setVisibility(View.VISIBLE);
                TextView nameText5 = getView().findViewById(R.id.name5);
                nameText5.setText(student.getName());
                TextView usernameText5 = getView().findViewById(R.id.username_hidden5);
                usernameText5.setText(student.getUserName());
                if (mentor) {
                    getView().findViewById(R.id.mentor5).setVisibility(View.VISIBLE);
                }
                break;
            case 5:
                getView().findViewById(R.id.student_card6).setVisibility(View.VISIBLE);
                TextView nameText6 = getView().findViewById(R.id.name6);
                nameText6.setText(student.getName());
                TextView usernameText6 = getView().findViewById(R.id.username_hidden6);
                usernameText6.setText(student.getUserName());
                if (mentor) {
                    getView().findViewById(R.id.mentor6).setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public int getTitle() {
        final String cs5001 = getString(R.string.course_5001);
        final String cs5003 = getString(R.string.course_5003);
        final String cs5004 = getString(R.string.course_5004);

        if (courseName.equals(cs5001)) {
            return R.string.course_5001;
        } else if (courseName.equals(cs5003)) {
            return R.string.course_5003;
        } else if (courseName.equals(cs5004)) {
            return R.string.course_5004;
        } else {
            return R.string.course_5006;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        createCourse();
        getActivity().setTitle(getTitle());
    }
}