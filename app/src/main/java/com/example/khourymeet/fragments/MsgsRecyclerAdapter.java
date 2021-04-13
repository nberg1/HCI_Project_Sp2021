package com.example.khourymeet.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khourymeet.R;

import java.util.ArrayList;

public class MsgsRecyclerAdapter extends RecyclerView.Adapter<MsgsRecyclerHolder> {
    private ArrayList<MessageCard> messageList;
    private ItemClickListener listener;


    public MsgsRecyclerAdapter(ArrayList<MessageCard> messages) {
        this.messageList = messages;
    }

    public void setOnClickItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public MsgsRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_card, parent, false);
        return new MsgsRecyclerHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgsRecyclerHolder holder, int position) {
        MessageCard currentCard = messageList.get(position);
        holder.name.setText(currentCard.getName());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
