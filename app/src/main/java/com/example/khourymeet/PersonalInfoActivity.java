package com.example.khourymeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText name;
    private EditText email;
    private EditText userName;
    private EditText password;
    private final String TAG = "MDPersonalInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.name_input);
        email = findViewById(R.id.email_input);
        userName = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
    }

    public void onClick(View view) {

        final String usernameString = userName.getText().toString();
        final String nameString = name.getText().toString();
        final String emailString = email.getText().toString();
        final String passwordString = password.getText().toString();

        if (!nameString.matches("^[a-zA-Z]*$") || nameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
        } else if (emailString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Northeastern email", Toast.LENGTH_LONG).show();
        } else if (passwordString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_LONG).show();
        } else if (!usernameString.matches("^[a-zA-Z0-9]*$") || usernameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an alphanumeric username", Toast.LENGTH_LONG).show();
        } else {
            writePersonalInfo(usernameString, nameString, emailString, passwordString);
        }
    }

    private void writePersonalInfo(String username, String name, String email, String password) {
        databaseReference.child("users").child(username).child("userName").setValue(username);
        databaseReference.child("users").child(username).child("name").setValue(name);
        databaseReference.child("users").child(username).child("email").setValue(email);
        databaseReference.child("users").child(username).child("password").setValue(password);
        Intent intent = new Intent(PersonalInfoActivity.this, AcademicInfoActivity.class);
        startActivity(intent);
    }
}