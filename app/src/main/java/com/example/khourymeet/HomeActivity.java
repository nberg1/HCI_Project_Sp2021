package com.example.khourymeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khourymeet.fragments.CoursePageFragment;
import com.example.khourymeet.fragments.DirectMessageFragment;
import com.example.khourymeet.fragments.EditProfileFragment;
import com.example.khourymeet.fragments.GroupMessage1Fragment;
import com.example.khourymeet.fragments.GroupMessage2Fragment;
import com.example.khourymeet.fragments.GroupsFragment;
import com.example.khourymeet.fragments.HomeFragment;
import com.example.khourymeet.fragments.MessagesFragment;
import com.example.khourymeet.fragments.NewMessageFragment;
import com.example.khourymeet.fragments.OtherUserProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.example.khourymeet.fragments.NavigationFragment;
import com.example.khourymeet.fragments.ProfileFragment;
import com.example.khourymeet.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT";
    BottomNavigationView bottomNavigation;
    private Fragment activeFragment;

    private SharedPreferences sharedPreferences;
    // TODO: use current username as key to pass academic info to db
    private String currentUsername;
    private final String defaultString = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, ACTIVE_FRAGMENT);
            if (fragment != null) {
                openFragment(fragment);
                return;
            }
        }
        openFragment(HomeFragment.newInstance());
    }

    public void openFragment(Fragment fragment) {
        this.activeFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        bottomNavigation.getMenu().getItem(2).setChecked(true);

        try {
            this.setTitle(((NavigationFragment) fragment).getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(HomeFragment.newInstance());
                            return true;
                        case R.id.navigation_search:
                            openFragment(SearchFragment.newInstance());
                            return true;
                        case R.id.navigation_messages:
                            openFragment(MessagesFragment.newInstance());
                            return true;
                        case R.id.navigation_groups:
                            openFragment(GroupsFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void courseButton(View view) {
        String courseName = "";
        switch (view.getId()) {
            case R.id.course1_button:
                TextView t = view.findViewById(R.id.course1_button);
                courseName = t.getText().toString();
                break;
            case R.id.course2_button:
                TextView t2 = view.findViewById(R.id.course2_button);
                courseName = t2.getText().toString();
                break;
        }
        openFragment(CoursePageFragment.newInstance(courseName, ""));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, ACTIVE_FRAGMENT, this.activeFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_signout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Are you sure you want to exit KhouryMeet?");
            builder.setCancelable(true);
            builder.setPositiveButton("Continue using App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Exit KhouryMeet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(Color.RED);
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.BLUE);
        }

        return super.onOptionsItemSelected(item);

    }

    // Go from Profile fragment to EditProfile fragment
    public void editProfileButton(View view) {
        openFragment(EditProfileFragment.newInstance("", ""));
    }

    // Go from EditProfile fragment to Profile fragment
    public void editToProfile(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.back_warning));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.back_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openFragment(ProfileFragment.newInstance("", ""));
            }
        });
        builder.setNegativeButton(getString(R.string.back_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.BLUE);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.RED);
    }

    // Go from search to random user profile
    public void searchToOtherProfile(View view) {
        openFragment(OtherUserProfileFragment.newInstance("", ""));
    }

    // Go from user profile to new direct message conversation
    public void otherProfileToDM(View view) {
        openFragment(DirectMessageFragment.newInstance("", ""));
    }

    public void futureIterationMessage(View view) {
        Toast.makeText(getApplicationContext(), "Due to backend complexity and time constraint, this functionality will be implemented in the future", Toast.LENGTH_LONG).show();
    }

    public void openGroupMessage1(View view) {
        openFragment(GroupMessage1Fragment.newInstance("", ""));
    }

    public void openGroupMessage2(View view) {
        openFragment(GroupMessage2Fragment.newInstance("", ""));
    }

    // Go from course to selected student user profile
    public void courseToUserProfile(View view) {
        String otherUsername = "";
        switch(view.getId()) {
            case R.id.profile_btn1:
                TextView t1 = this.findViewById(R.id.username_hidden1);
                otherUsername = t1.getText().toString();
                break;
            case R.id.profile_btn2:
                TextView t2 = this.findViewById(R.id.username_hidden2);
                otherUsername = t2.getText().toString();
                break;
            case R.id.profile_btn3:
                TextView t3 = this.findViewById(R.id.username_hidden3);
                otherUsername = t3.getText().toString();
                break;
            case R.id.profile_btn4:
                TextView t4 = this.findViewById(R.id.username_hidden4);
                otherUsername = t4.getText().toString();
                break;
            case R.id.profile_btn5:
                TextView t5 = this.findViewById(R.id.username_hidden5);
                otherUsername = t5.getText().toString();
                break;
            case R.id.profile_btn6:
                TextView t6 = this.findViewById(R.id.username_hidden6);
                otherUsername = t6.getText().toString();
                break;
        }
//        TextView text = this.findViewById(R.id.student_username_hidden);
//        String usernameHidden = text.getText().toString();
        if (!otherUsername.equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.other_username_preferences_key), otherUsername);
            editor.apply();
            openFragment(OtherUserProfileFragment.newInstance("", ""));
        }
    }

    // Go from list of messages to the message of a single student
    public void dmsToSingleConvo(View view) {
        String otherUsername = "";
        switch(view.getId()) {
            case R.id.open_convo1:
                TextView t1 = this.findViewById(R.id.dm_username_hidden1);
                otherUsername = t1.getText().toString();
                break;
            case R.id.open_convo2:
                TextView t2 = this.findViewById(R.id.dm_username_hidden2);
                otherUsername = t2.getText().toString();
                break;
            case R.id.open_convo3:
                TextView t3 = this.findViewById(R.id.dm_username_hidden3);
                otherUsername = t3.getText().toString();
                break;
            case R.id.open_convo4:
                TextView t4 = this.findViewById(R.id.dm_username_hidden4);
                otherUsername = t4.getText().toString();
                break;
        }
//        TextView text = this.findViewById(R.id.student_username_hidden);
//        String usernameHidden = text.getText().toString();
        if (!otherUsername.equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.other_username_preferences_key), otherUsername);
            editor.apply();
            openFragment(DirectMessageFragment.newInstance("", ""));
        }
    }

    // Go from list of direct messages to new direct message conversation
    public void dmListToAnonConvo(View view) {
        openFragment(NewMessageFragment.newInstance("", ""));
    }

}