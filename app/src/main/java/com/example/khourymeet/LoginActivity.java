package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
            String name = currentUser.getDisplayName();
            GetUser getUser = new GetUser();
            getUser.getUser(name);
        }
    }

    public void onClickLogin(View view) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
            String name = currentUser.getDisplayName();
            GetUser getUser = new GetUser();
            getUser.getUser(name);

        } else {
            final String userNameString = username.getText().toString();
            final String passwordString = password.getText().toString();

            if (userNameString.equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            } else if (passwordString.equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
            } else {
                firebaseAuth.signInWithEmailAndPassword(userNameString, passwordString)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String name = user.getDisplayName();
                                    GetUser getUser = new GetUser();
                                    getUser.getUser(name);
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "User does not exist. Please register a new user.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
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
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(final InstanceIdResult instanceIdResult) {
                    final String token = instanceIdResult.getToken();
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(username)) {
                                user = snapshot.child(username).getValue(User.class);

                                if (user.getToken() != null && !user.getToken().equals(token)) {
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
        Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
        intentHome.putExtra("USER_USERNAME", user.getUsername());
        intentHome.putExtra("USER_NAME", user.getName());
        intentHome.putExtra("USER_EMAIL", user.getEmail());
        intentHome.putExtra("USER_PASSWORD", user.getPassword());
        intentHome.putExtra("USER_TOKEN", user.getToken());
        startActivity(intentHome);
    }
}