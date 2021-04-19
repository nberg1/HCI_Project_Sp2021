package com.example.khourymeet.fragments;

public class StudentCard implements ItemClickListener {

    private String name;
    private String username;

    public StudentCard(String name) {
        this.name = name;
    }

    public StudentCard(String name, String username) { this.name = name; this.username = username; }

    public String getName() { return this.name; }

    public String getUsername() { return this.username; }

    // Clicking on somewhere within item
    @Override
    public void onItemClick(int position) {
        // should actually take the user to the web page they entered
        return;
    }
}
