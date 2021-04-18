package com.example.khourymeet.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khourymeet.R;

import java.util.ArrayList;

public class StudentsRecyclerAdapter extends RecyclerView.Adapter<StudentsRecyclerHolder> {
    private ArrayList<StudentCard> studentList;
    private ItemClickListener listener;


    public StudentsRecyclerAdapter(ArrayList<StudentCard> messages) {
        this.studentList = messages;
    }

    public void setOnClickItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public StudentsRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_card, parent, false);
        return new StudentsRecyclerHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsRecyclerHolder holder, int position) {
        StudentCard currentCard = studentList.get(position);
        holder.name.setText(currentCard.getName());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
