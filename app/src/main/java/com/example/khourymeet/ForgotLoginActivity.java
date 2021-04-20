package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ForgotLoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DataSnapshot userSnapshot;

    /*
    Will match Northeastern email
    Format: last name + period + portion of first name + @northeastern.edu
    */
    private static final String emailPattern = "([a-z\\-])+(?<![\\-])(\\.)([a-z\\-])+([0-9]+)?(?<![\\-])(@northeastern.edu)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_login);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        final EditText emailView = findViewById(R.id.type_email_here);

        Button sendEmailButton = findViewById(R.id.send_email_button);
        Button backButton = findViewById(R.id.go_back_button);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailValid(emailView.getText().toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ForgotLoginActivity.this, LoginActivity.class);
                ForgotLoginActivity.this.startActivity(loginIntent);
            }
        });
    }

    // Check that the email is a valid Northeastern email
    private void checkEmailValid(final String emailVal) {
        if (!emailVal.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Oops! This is not a valid Northeastern email address. Please try again.", Toast.LENGTH_LONG).show();
        } else {
            checkEmailExists(emailVal);
        }
    }

    // Check that email does exist in system
    private void checkEmailExists(final String emailVal) {
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
                        userSnapshot = childSnapshot;
                        break;
                    }
                }
                if (emailExists) {
                    emailSent(emailVal, userSnapshot);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no account with that email. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Set up dialog box for login email sent
    private void emailSent(String userEmail, DataSnapshot userSnapshot) {
        try {
            List toEmailList = Arrays.asList(userEmail
                    .split("\\s*,\\s*"));

            String userName = userSnapshot.child("userName").getValue().toString();
            String userPassword = userSnapshot.child("password").getValue().toString();

            String subject = "KhouryMeet Login Credentials";
            String message = "These are your credentials to login into KhouryMeet. " +
                    "Username: " + userName + " and " +
                    "Password: " + userPassword;

            new SendMailTask(ForgotLoginActivity.this).execute("khourymeet@gmail.com",
                    "HCI2021!", toEmailList, subject, message);


            Intent loginIntent = new Intent(ForgotLoginActivity.this, LoginActivity.class);
            ForgotLoginActivity.this.startActivity(loginIntent);
            Toast.makeText(getApplicationContext(), "Email sent successfully! Please login with the credential we just sent to your email", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("ForgotLoginActivity", e.toString());
        }





//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(getString(R.string.notify_email_sent));
//        builder.setCancelable(true);
//        builder.setPositiveButton(getString(R.string.invisible_understand), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        positiveButton.setTextColor(Color.BLUE);
    }
}