package com.example.khourymeet.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khourymeet.R;

public class MsgsRecyclerHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public Button open;

    public MsgsRecyclerHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.friend_name_convo);
        open = itemView.findViewById(R.id.open_convo);

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
