package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private SharedPreferences sharedPreferences;

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Used Android documentation on how to implement shared preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userName = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
    }

    public void onClick(View view) {

        final String userNameString = userName.getText().toString();
        final String passwordString = password.getText().toString();

        if (userNameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
        } else if (passwordString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
        } else {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(userNameString).exists() && snapshot.child(userNameString).child("password").getValue().equals(passwordString)) {
                        Toast.makeText(getApplicationContext(), "Successful Login!", Toast.LENGTH_SHORT).show();

                        // Referenced Android documentation to write username locally
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.username_preferences_key), userNameString);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Account does not exist. Please register a new user.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // empty on purpose
                }
            });
        }
    }

    public void forgotLoginInfo(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotLoginActivity.class);
        startActivity(intent);
    }
}