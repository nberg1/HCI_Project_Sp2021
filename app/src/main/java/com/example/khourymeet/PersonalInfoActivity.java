package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "User is still signed in", Toast.LENGTH_LONG).show();
            String name = currentUser.getDisplayName();
            GetUser getUser = new GetUser();
            getUser.getUser(name);
        }
    }

    public void onClick(View view) {

        SignUpUser signUpUser = new SignUpUser();

        final String usernameString = userName.getText().toString();
        final String nameString = name.getText().toString();
        final String emailString = email.getText().toString();
        final String passwordString = password.getText().toString();

        if (!nameString.matches("^[a-zA-Z0-9]*$") || nameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an alphanumeric name", Toast.LENGTH_LONG).show();
        } else if (emailString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_LONG).show();
        } else if (passwordString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
        } else if (!usernameString.matches("^[a-zA-Z0-9]*$") || usernameString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an alphanumeric username", Toast.LENGTH_LONG).show();
        } else {
            signUpUser.signUpUser(usernameString, nameString, emailString, passwordString);
        }
    }

    private class SignUpUser implements Runnable {
        private String username;
        private String name;
        private String email;
        private String password;
        private User user;

        SignUpUser() {
        }

        public void signUpUser(String username, String name, String email, String password) {
            this.username = username;
            this.name = name;
            this.email = email;
            this.password = password;
            this.runInNewThread();
        }

        @Override
        public void run() {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });

                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(PersonalInfoActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(final InstanceIdResult instanceIdResult) {
                                        final String token = instanceIdResult.getToken();
                                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                if (snapshot.hasChild(username)) {
                                                    user = snapshot.child(username).getValue(User.class);

                                                    if (!user.getToken().equals(token)) {
                                                        user.setToken(token);
                                                        databaseReference.child("users").child(user.getUsername()).setValue(user);
                                                    }
                                                } else {
                                                    user = new User(name, email, username, password, token);
                                                    databaseReference.child("users").child(user.getUsername()).setValue(user);
                                                }
                                                startIntent(user);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            } else {
                                Toast.makeText(PersonalInfoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }

        public void runInNewThread() {
            new Thread(this).start();
        }
    }

    private class GetUser implements Runnable {

        private String username;
        private User user;

        public GetUser() {
        }

        public void getUser(String username) {
            this.username = username;
            this.runInNewThread();
        }

        @Override
        public void run() {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(PersonalInfoActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(final InstanceIdResult instanceIdResult) {
                    final String token = instanceIdResult.getToken();
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(username)) {
                                user = snapshot.child(username).getValue(User.class);

                                if (!user.getToken().equals(token)) {
                                    user.setToken(token);
                                    databaseReference.child("users").child(user.getUsername()).setValue(user);
                                }
                                startIntent(user);
                            } else {
                                Toast.makeText(getApplicationContext(), "User exists in Auth but not in the Database", Toast.LENGTH_SHORT).show();
                            }
                            startIntent(user);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        public void runInNewThread() {
            new Thread(this).start();
        }
    }

    private void startIntent(User user) {
        Intent intentHome = new Intent(PersonalInfoActivity.this, AcademicInfoActivity.class);
        intentHome.putExtra("USER_USERNAME", user.getUsername());
        intentHome.putExtra("USER_NAME", user.getName());
        intentHome.putExtra("USER_EMAIL", user.getEmail());
        intentHome.putExtra("USER_PASSWORD", user.getPassword());
        intentHome.putExtra("USER_TOKEN", user.getToken());
        startActivity(intentHome);
    }
}