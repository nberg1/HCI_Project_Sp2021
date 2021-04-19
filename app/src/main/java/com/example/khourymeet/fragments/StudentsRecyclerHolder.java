package com.example.khourymeet.fragments;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khourymeet.R;

public class StudentsRecyclerHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView username;
    private static final String ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT";
    private Fragment activeFragment;

    public StudentsRecyclerHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.student_name);
        username = itemView.findViewById(R.id.student_username_hidden);
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    listener.onItemClick(position);
                    Log.w("Click user button: ", name.getText().toString());
                    Log.w("Click username button: ", username.getText().toString());
                }
            }
        });
    }
}
