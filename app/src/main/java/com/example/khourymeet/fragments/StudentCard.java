package com.example.khourymeet.fragments;

public class StudentCard implements ItemClickListener {

    private String name;
    private String username;
    private boolean mentor;

    public StudentCard(String name) {
        this.name = name;
    }

    public StudentCard(String name, String username) { this.name = name; this.username = username; this.mentor = false; }

    public StudentCard(String name, String username, boolean mentor) {
        this.name = name;
        this.username = username;
        this.mentor = mentor;
    }
    public String getName() { return this.name; }

    public String getUsername() { return this.username; }

    public boolean getType() { return this.mentor; }
    // Clicking on somewhere within item
    @Override
    public void onItemClick(int position) {
        // should actually take the user to the web page they entered
        return;
    }
}
