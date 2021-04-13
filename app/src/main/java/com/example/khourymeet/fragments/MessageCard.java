package com.example.khourymeet.fragments;

public class MessageCard implements ItemClickListener {

    private String name;

    public MessageCard(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    // Clicking on somewhere within item
    @Override
    public void onItemClick(int position) {
        // should actually take the user to the web page they entered
        return;
    }

}
