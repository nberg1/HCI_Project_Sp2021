package com.example.khourymeet.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.khourymeet.R;

public class StudentsRecyclerHolder extends RecyclerView.ViewHolder{

    public TextView name;

    public StudentsRecyclerHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.student_name);
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    listener.onItemClick(position);
                }
            }
        });
    }

}
