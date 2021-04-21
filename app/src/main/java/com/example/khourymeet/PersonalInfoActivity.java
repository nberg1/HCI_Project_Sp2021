package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText name;
    private EditText email;
    private EditText userName;
    private EditText password;

    private SharedPreferences sharedPreferences;

    private final String TAG = "MDPersonalInfoActivity";

    private AlertDialog alertDialog;

    /*
        Will match names with at least one space (requires first and last)
        Does not check for case (first letter doesn't have to be capitalized)
        Can't end with a space or a hyphen
        More than 2 names allowed, and all are able to have hyphens
        Should allow letters wih accent marks
     */
    private static final String NAME_PATTERN = "([\\p{L}\\-])+(\\s)([\\p{L}\\-\\s])+(?<![\\-\\s])";

    /*
        Will match Northeastern email
        Format: last name + period + portion of first name + @northeastern.edu
     */
    private static final String EMAIL_PATTERN = "([a-z\\-])+(?<![\\-])(\\.)([a-z\\-])+([0-9]+)?(?<![\\-])(@northeastern.edu)";


    /*
        Will make sure passwords entered by the user are secure and have at least one uppercase letter, a number and a symbol
     */
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    /*
        Checks username
        Allows for uppercase and lowercase letters (without accents) and certain special characters
        Does not allow spaces
     */
    private static final String USERNAME_PATTERN = "([a-zA-Z0-9\\-_!*@]+)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Used Android documentation on how to implement shared preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

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
        if (!nameString.matches(NAME_PATTERN) || nameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your full name", Toast.LENGTH_LONG).show();
        } else if (!emailString.matches(EMAIL_PATTERN) || emailString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Northeastern email", Toast.LENGTH_LONG).show();
        } else if (!isValidPassword(passwordString)) {
            Toast.makeText(getApplicationContext(), "Please enter a valid password. Click info icon for more details", Toast.LENGTH_LONG).show();
        } else if (!usernameString.matches(USERNAME_PATTERN) || usernameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a username with only letters, numbers, and/or the following: - . _ ! * @", Toast.LENGTH_LONG).show();
        } else {
            writePersonalInfo(usernameString, nameString, emailString, passwordString);
        }
    }


    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        Log.i("Password is good:", String.valueOf(matcher.matches()));
        return matcher.matches();
    }

    // Write personal information to database and store username locally
    private void writePersonalInfo(String username, String name, String email, String password) {
        databaseReference.child("users").child(username).child("userName").setValue(username);
        databaseReference.child("users").child(username).child("name").setValue(name);
        databaseReference.child("users").child(username).child("email").setValue(email);
        databaseReference.child("users").child(username).child("password").setValue(password);

        // Referenced Android documentation to write username locally
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.username_preferences_key), username);
        editor.apply();

        Intent intent = new Intent(PersonalInfoActivity.this, AcademicInfoActivity.class);
        startActivity(intent);
    }

    public void passwordInfo(View view) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.password_security))
                .setMessage(getString(R.string.password_security_message))
                .setPositiveButton(getString(R.string.invisible_understand), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.BLUE);
    }
}