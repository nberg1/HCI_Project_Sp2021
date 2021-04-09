package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.Objects;

public class PersonalInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText name;
    private EditText email;
    private EditText userName;
    private EditText password;
    private final String TAG = "MDPersonalInfoActivity";

    // Regex Expressions

    // Will match names with at least one space (requires first and last)
    // Does not check for case (first letter doesn't have to be capitalized)
    // Can't end with a space or a hyphen
    // More than 2 names allowed, and all are able to have hyphens
    // Should allow letters wih accent marks
    // TODO: figure out how to only allow one hyphen per word
    private static final String namePattern = "([\\p{L}\\-])+(\\s)([\\p{L}\\-\\s])+(?<![\\-\\s])";
    // Will match Northeastern email
    // Format: last name + period + portion of first name + @northeastern.edu
    // TODO: figure out how to only allow one hyphen per word
    private static final String emailPattern = "([a-z\\-])+(?<![\\-])(\\.)([a-z\\-])+([0-9]+)?(?<![\\-])(@northeastern.edu)";
    // Checks username
    // Allows for uppercase and lowercase letters (without accents) and certain special characters
    // Does not allow spaces
    private static final String usernamePattern = "([a-zA-Z0-9\\-_!*@]+)";

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

        checkUsernameExists(usernameString, nameString, emailString, passwordString);
    }

    private void writePersonalInfo(String username, String name, String email, String password) {
        databaseReference.child("users").child(username).child("userName").setValue(username);
        databaseReference.child("users").child(username).child("name").setValue(name);
        databaseReference.child("users").child(username).child("email").setValue(email);
        databaseReference.child("users").child(username).child("password").setValue(password);
        Intent intent = new Intent(PersonalInfoActivity.this, AcademicInfoActivity.class);
        startActivity(intent);
    }

    // Check that username does not already exist
    private void checkUsernameExists(final String usernameVal, final String nameString, final String emailString, final String passwordString) {
        databaseReference.child(getString(R.string.users_path,
                usernameVal)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to check if username exists
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    checkEmailExists(usernameVal, nameString, emailString, passwordString);
                } else {
                    Toast.makeText(getApplicationContext(), "This username already exists. Please choose another one.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Check that email does not already exist
    private void checkEmailExists(final String usernameString, final String nameString, final String emailVal, final String passwordString) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to go through users and see if email already exists
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean emailExists = false;
                // Get snapshot of each child and check if the email input matches an email that
                // already exists
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(childSnapshot.child("email").getValue(), emailVal)) {
                        emailExists = true;
                        break;
                    }
                }
                if (!emailExists) {
                    generalChecks(usernameString, nameString, emailVal, passwordString);
                } else {
                    Toast.makeText(getApplicationContext(), "There is already an account with that email. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Run other validity checks
    private void generalChecks(String usernameString, String nameString, String emailString, String passwordString) {
        if (!nameString.matches(namePattern) || nameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
        } else if (!emailString.matches(emailPattern) || emailString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Northeastern email", Toast.LENGTH_LONG).show();
        } else if (passwordString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_LONG).show();
        } else if (!usernameString.matches(usernamePattern) || usernameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a username with only letters, numbers, and/or the following: - . _ ! * @", Toast.LENGTH_LONG).show();
        } else {
            writePersonalInfo(usernameString, nameString, emailString, passwordString);
        }
    }

}