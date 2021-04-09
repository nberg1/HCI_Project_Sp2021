package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

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

        checkEmailExistsTest("marielleTest");
    }

    public void onClick(View view) {

        final String usernameString = userName.getText().toString();
        final String nameString = name.getText().toString();
        final String emailString = email.getText().toString();
        final String passwordString = password.getText().toString();

        if (!nameString.matches(namePattern) || nameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
        } else if (!emailString.matches(emailPattern) || emailString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Northeastern email", Toast.LENGTH_LONG).show();
        } else if (passwordString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_LONG).show();
        } else if (!usernameString.matches("^[a-zA-Z0-9]*$") || usernameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an alphanumeric username", Toast.LENGTH_LONG).show();
        } else if (checkUsernameExists(usernameString)) {
            Toast.makeText(getApplicationContext(), "This username already exists. Please choose another one.", Toast.LENGTH_LONG).show();
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

    // Check that username does not already exist
    private boolean checkUsernameExists(final String usernameVal) {
        final boolean[] usernameExists = new boolean[1];
        databaseReference.child(getString(R.string.users_path,
                usernameVal)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to check if username exists
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernameExists[0] = snapshot.exists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled");
            }
        });
        return usernameExists[0];
    }

    private boolean checkEmailExistsTest(final String usernameVal) {
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    assert key != null;
                    if (key.equals(usernameVal)) {
                        Log.w("snapshot", String.valueOf(childSnapshot));
                        Log.w("snapshot key", childSnapshot.getKey());
                        User3 userToCheck = childSnapshot.getValue(User3.class);
                        Log.w("User3 Align", userToCheck.getAlign().toString());
                        Log.w("User3 Email", userToCheck.getEmail());
                        Log.w("User3 Image", userToCheck.getImage());
                        Log.w("User3 First Semester", userToCheck.getFirstSemester());
                        Log.w("User3 Name", userToCheck.getName());
                        Log.w("User3 Username", userToCheck.getUsername());
                        Log.w("User3 Password", userToCheck.getPassword());
                        Log.w("User3 Courses", userToCheck.getCourses().toString());
                    }
                    // Data Snapshot: DataSnapshot { key = dummyData, value = {courses={0=5001, 1=5003}, image=imageFileString, password=1234, name=dummy, align=true, firstSemester=Spring 2020, email=dummy@data.com, friends={0=dummy@data2.com}, username=dummydata} }
//                    User2 userToCheck = childSnapshot.getValue(User2.class);
//                    Log.w("snapshot email", userToCheck.getEmail());
//                    Log.w("snapshot courses", userToCheck.getCourses().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled");
            }
        });
        return true;
    }

    // Check that email does not already exist
    private boolean checkEmailExists(final String emailVal) {
        final boolean[] emailExists = new boolean[1];
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            // Use snapshot to go through users and see if email already exists
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get snapshot of each child and check if the email input matches an email that
                // already exists
                int count = 0;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (count == 1) {
                        break;
                    }
                    Log.w("snapshot", String.valueOf(childSnapshot));
                    // TODO: using User.class, get error saying "cannot convert long to string",
                    //  but using User2 (HashMaps instead of lists for courses and friends) get
                    //  error saying "Expected a Map while deserializing, but got a class
                    //  java.util.ArrayList".
                    // Data Snapshot: DataSnapshot { key = dummyData, value = {courses={0=5001, 1=5003}, image=imageFileString, password=1234, name=dummy, align=true, firstSemester=Spring 2020, email=dummy@data.com, friends={0=dummy@data2.com}, username=dummydata} }
                    User2 userToCheck = childSnapshot.getValue(User2.class);
//                    Log.w("snapshot email", userToCheck.getEmail());
//                    Log.w("snapshot courses", userToCheck.getCourses().toString());
                    count ++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled");
            }
        });
//        return emailExists[0];
        return true;
    }

}